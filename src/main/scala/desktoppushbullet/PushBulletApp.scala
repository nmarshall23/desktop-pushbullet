package desktoppushbullet

import scala.swing.Frame
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import desktoppushbullet.dialogs.PushLinkDialog
import desktoppushbullet.dialogs.NoteDialog

object PushBulletApp {

  val system = ActorSystem("MySystem")
  val mainloop = system.actorOf(Props[appLoop], name = "AppLoop")
  val pushapi  = system.actorOf(Props[PushAPI], name = "PushAPI")
  
  def main(args: Array[String]): Unit = {
    mainloop ! Setup
  }
  
  def shutdown {
    PushBulletApp.system.shutdown()
    sys.exit(0)
  }

  def setupGUI {
    val pref = new PreferencesDialog(false)
    val dialogs = Set[Frame](new PushLinkDialog, new NoteDialog)

    val tray = new BulletSystemTray(dialogs, pref)
  }
}

abstract class Commands
object Setup extends Commands
object Quit extends Commands

class appLoop extends Actor {
  def receive = {
    case Setup if (Preferences.API_KEY_Option.isEmpty) => new PreferencesDialog(true)
    case Setup if (Preferences.DefaultDeviceId_Option.isEmpty) => PushBulletApp.pushapi ! GetDevices
    case Setup => PushBulletApp.setupGUI
    case Quit  => PushBulletApp.shutdown

  }
  

}