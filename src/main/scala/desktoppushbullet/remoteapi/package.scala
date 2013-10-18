package desktoppushbullet

package object remoteapi {
  type MenuItemCmd = (String, Function0[Unit])

  


  trait ApiCall
   // extends ApiCall
  
  object Pushable{
    case class Note(title: String, body: String) extends ApiCall
    case class Link(title: String, url: String) extends ApiCall
    case class Address(name: String, address: String) extends ApiCall
    case class ToDoList(tile: String, items: String) extends ApiCall
    case class File(file: String) extends ApiCall
  }
  
  object jsonClasses {
    import argonaut._, Argonaut._
    
    implicit def ListOfDevicesCodecJson: CodecJson[ListOfDevices] =
        casecodec2(ListOfDevices.apply, ListOfDevices.unapply)("devices", "shared_devices")
    
    implicit def ExtraInfoCodecJson: CodecJson[DeviceExtraInfo] =
        casecodec5(DeviceExtraInfo.apply, DeviceExtraInfo.unapply)("android_version", "app_version", "manufacturer", "model", "sdk_version")
    
    implicit def DevicesCodecJson: CodecJson[Devices] =
        casecodec2(Devices.apply, Devices.unapply)("id", "extras")
        
    
    case class ListOfDevices(devices: List[Devices], shared_devices: List[Devices])
    case class Devices(id: Int, extra: DeviceExtraInfo)
    case class DeviceExtraInfo(android_version: String, app_version: String, manufacturer: String, model: String, sdk_version: String)

      
  }
}