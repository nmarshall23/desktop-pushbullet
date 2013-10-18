package desktoppushbullet


import org.streum.configrity._
import org.streum.configrity.io.StandardFormat.ParserException
import Option.{apply => ?}
import java.io.File
import scalaz.IsEmpty

object Preferences {
 
  //Damnit can't think of away handle updates without making this a var.
  private var config = try {
    Configuration.load(ConfigName, org.streum.configrity.io.BlockFormat)
  } catch {
    case e:java.io.FileNotFoundException => Configuration()
    case e:ParserException => Configuration() //Hmm Guess it's better to overwrite the configuration. 
  }


  
  def setAPI_KEY(key:String):Boolean = {
    config = config.set[String]("users_api_key",key)
    config.save(ConfigName)
    config.contains("users_api_key")
  }
  
  def setDevice(device:Devices):Boolean = {
    val manufacturer = device.extra.manufacturer
    val model = device.extra.model
    val name = s""""$manufacturer $model"""" 
    config = config.set[Int]("device.id"  , device.id)
       .set[String]("device.name", name )
       
    config.save(ConfigName)
    config.contains("device.id")
    
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
  
  private lazy val ConfigName = {
   val configFileName = "pushbullet_desktop.conf"
   val userHomeDir = ?(System.getenv("USERPROFILE") ).getOrElse( ?(System.getProperty("user.home") ).getOrElse(".") )
   val ourConfDir = System.getProperty("os.name") match {
      case "Linux" => ".local" + File.separator + "share" + File.separator + "pushbullet_desktop"
      case _ => ".pushbullet_desktop"
   }
    
   val configDir =  userHomeDir + File.separator + ourConfDir
   
   def checkConfigDirExists(configDir:File):Boolean = configDir.isDirectory() || configDir.mkdirs()
   
   //println("Config Name: " + configDir)
   //println("Config dir: " + checkConfigDirExists(new File(configDir)) )
   //Not sure what else to do if configdir doesn't exists. Just return the configFileName
   if(checkConfigDirExists(new File(configDir)) ) configDir + File.separator + configFileName
   else configFileName
   
  }
}