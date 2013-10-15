package desktoppushbullet

import dispatch._, Defaults._

abstract class PushableType
case class Note(title:String, body:String) extends PushableType
case class Link(title:String, url:String) extends PushableType
case class Address(name:String, address:String) extends PushableType
case class List(tile:String, items:String) extends PushableType
case class File(file:String) extends PushableType

case class Device(name:String, id:Int)

class PushbulletAPI(API_KEY:String) {

  type StatusCode = Int
  private val apiURL = :/("www.pushbullet.com").secure.as_!(API_KEY, "foo") / "api" 
  private val devicesAPI = apiURL / "devices"
  private val pushAPI = apiURL / "pushes"
  
  def getDevices:(Device,Device,StatusCode) = {
   
   
   
   val devices = Http(devicesAPI.GET OK as.String).option
   for(d <- devices() ) println(d)
 //  result.status
   
   null
  }
  
  def push(device_id:Int, push:PushableType) = {
    
    val params = push match {
    	case Note(title, body) => Map("title" -> title, "body" -> body)
    	case Link(title, url)  => Map("title" -> title, "url" -> url)
    	case Address(name, address) => Map("name" -> name, "address" -> address)
    }
    
    val res = Http(pushAPI << params OK as.String)
    
    res()
  }

}