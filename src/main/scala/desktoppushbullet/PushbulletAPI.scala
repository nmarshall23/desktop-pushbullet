package desktoppushbullet

import scala.collection.immutable.Map
import akka.actor.Actor
import dispatch._, Defaults._
import argonaut._, Argonaut._

abstract class PushableType extends ApiCall
case class Note(title:String, body:String) extends PushableType
case class Link(title:String, url:String) extends PushableType
case class Address(name:String, address:String) extends PushableType
case class ToDoList(tile:String, items:String) extends PushableType
case class File(file:String) extends PushableType

abstract class ApiCall
case class GetDevices(API_KEY:String) extends ApiCall


class PushAPI extends Actor {
  lazy val key = Preferences.API_KEY
  lazy val pushAPI = new PushbulletAPI(key)
  lazy val deviceId = Preferences.DefaultDeviceId
  
  def receive = {
    case GetDevices => saveDevice
    case ToDoList	=>
    case File(file)		    => pushAPI.push(deviceId, Map("type" -> "file", "file" -> file) )
    case Note(title, body) => pushAPI.push(deviceId, Map("type" -> "note", "title" -> title, "body" -> body) )
    case Link(title, url)  => pushAPI.push(deviceId, Map("type" -> "link","title" -> title, "url" -> url) )
    case Address(name, address) => pushAPI.push(deviceId, Map("type" -> "address", "name" -> name, "address" -> address) )
  }
  
  def saveDevice {
    val res = pushAPI.getDevices

    for(
     json <- res;
     maybeList = json.decodeOption[ListOfDevices];
     listOf <- maybeList;
     first <- listOf.devices.headOption
    ) Preferences.setDevice(first)
   
  }  
  
}



case class ListOfDevices(devices:List[Devices], shared_devices:List[Devices])
case class Devices(id:Int, extra:DeviceExtraInfo)
case class DeviceExtraInfo(android_version:String, app_version:String, manufacturer:String, model:String, sdk_version:String)


object ListOfDevices {
  implicit def ListOfDevicesCodecJson: CodecJson[ListOfDevices] =
    casecodec2(ListOfDevices.apply, ListOfDevices.unapply)("devices", "shared_devices")
}

object DeviceExtraInfo {
  implicit def ExtraInfoCodecJson: CodecJson[DeviceExtraInfo] =
    casecodec5(DeviceExtraInfo.apply, DeviceExtraInfo.unapply)("android_version", "app_version", "manufacturer", "model", "sdk_version")
}

object Devices {
  implicit def DevicesCodecJson: CodecJson[Devices] =
    casecodec2(Devices.apply, Devices.unapply)("id", "extras")
}




class PushbulletAPI(API_KEY:String) {

  type StatusCode = Int
  private val apiURL = :/("www.pushbullet.com").secure.as_!(API_KEY, "foo") / "api" 
  private val devicesAPI = apiURL / "devices"
  private val pushAPI = apiURL / "pushes"
  
  def getDevices = {

   val devices = Http(devicesAPI.GET OK as.String)
  // for(d <- devices() ) println(d)
 //  result.status
   devices
  }
  
  def push(device_id:Int, params:Map[String,String]) = {
	val devid = Map("device_id" -> device_id.toString)
	val inter = params ++ devid
    val res = Http(pushAPI << inter OK as.String)
    
    res()
  }

}