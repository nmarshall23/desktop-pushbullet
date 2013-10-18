package desktoppushbullet

import scala.collection.immutable.Map
import akka.actor.Actor
import dispatch._, Defaults._
import argonaut._, Argonaut._
import scala.util.Success
import scala.util.Failure
//import desktoppushbullet.QuitWithError
import desktoppushbullet.remoteapi.Pushable
import desktoppushbullet.remoteapi.jsonClasses._

case class GetDevices

class PushAPI extends Actor {
  lazy val key = Preferences.API_KEY
  lazy val pushAPI = new PushbulletAPI(key)
  lazy val deviceId = Preferences.DefaultDeviceId

  def receive = {
    case GetDevices => saveDevice
    case Pushable.ToDoList(title, items) =>
    case Pushable.File(file) => pushAPI.push(deviceId, Map("type" -> "file", "file" -> file))
    case Pushable.Note(title, body) => pushAPI.push(deviceId, Map("type" -> "note", "title" -> title, "body" -> body))
    case Pushable.Link(title, url) => pushAPI.push(deviceId, Map("type" -> "link", "title" -> title, "url" -> url))
    case Pushable.Address(name, address) => pushAPI.push(deviceId, Map("type" -> "address", "name" -> name, "address" -> address))
  }

  def saveDevice = pushAPI.getDevices onComplete {
   
    case Success(json) => {
      
      for (
        listOf <- json.decodeOption[ListOfDevices];
        first <- listOf.devices.headOption
      ) Preferences.setDevice(first) 

      PushBulletApp.mainloop ! StartAPP
    }
    case Failure(t) => {
      println("An error has occured: " + t.getMessage)
      
      PushBulletApp.mainloop ! QuitWithError(t.getMessage)
    }
  }

}



class PushbulletAPI(API_KEY: String) {

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

  def push(device_id: Int, params: Map[String, String]) = {
    val devid = Map("device_id" -> device_id.toString)
    val inter = params ++ devid
    val res = Http(pushAPI << inter OK as.String)

    res()
  }

}