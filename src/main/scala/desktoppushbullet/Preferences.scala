package desktoppushbullet


import org.streum.configrity._
import org.streum.configrity.io.StandardFormat.ParserException

object Preferences {
  private val configFileName = "pushbullet_desktop.conf"

  private val config = try {
    Configuration.load(configFileName, org.streum.configrity.io.BlockFormat)
  } catch {
    case e:java.io.FileNotFoundException => Configuration()
    case e:ParserException => Configuration() //Hmm Guess it's better to overwrite the configuration. 
  }


  
  def setAPI_KEY(key:String) = config.set[String]("users_api_key",key).save(configFileName)
  def setDevice(device:Devices) {
    val manufacturer = device.extra.manufacturer
    val model = device.extra.model
    val name = s""""$manufacturer $model"""" 
    config.set[Int]("device.id"  , device.id)
       .set[String]("device.name", name )
       .save(configFileName)
  }
  
  def NeedtoConfigure:Boolean = {
    val keys = List("users_api_key", "device.id","device.name")
    val keycheck = keys.forall( key => config.contains(key) ) //should be true 
    !keycheck
  }
  
  def API_KEY = config[String]("users_api_key")
  def API_KEY_Option = config.get[String]("users_api_key")
  
  def DefaultDeviceId = config[Int]("device.id",0)
  def DefaultDeviceId_Option = config.get[Int]("device.id")
  def DefaultDeviceName = config[String]("device.name")
  
}