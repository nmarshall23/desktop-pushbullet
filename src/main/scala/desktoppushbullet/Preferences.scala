package desktoppushbullet


import org.streum.configrity._

object Preferences {
  private val configFileName = "app.conf"

  private val config = try {
    Configuration.load(configFileName, org.streum.configrity.io.BlockFormat)
  } catch {
    case e:java.io.FileNotFoundException => Configuration()
  }


  
  def setAPI_KEY(key:String) = config.set[String]("users_api_key",key).save(configFileName)
  def setDevice(device:Devices) {
    config.set[Int]("device.id"  , device.id)
       .set[String]("device.name", "\"" + device.extra.manufacturer + " " + device.extra.model + "\"" )
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