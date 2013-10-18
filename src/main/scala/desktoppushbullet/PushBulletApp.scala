package desktoppushbullet

import scala.swing.Frame
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import desktoppushbullet.dialogs.PushLinkDialog
import desktoppushbullet.dialogs.NoteDialog
import scala.swing.RichWindow
import akka.actor.PoisonPill
import scala.concurrent.duration._
import desktoppushbullet.systemtray.BulletSystemTray
import desktoppushbullet.systemtray.TrayWidget

import desktoppushbullet.remoteapi._

object PushBulletApp {
  
  val actorSupervisor = ActorSystem("MySystem")
  val mainloop = actorSupervisor.actorOf(Props[appLoop], name = "AppLoop")
  val pushapi  = actorSupervisor.actorOf(Props[PushAPI], name = "PushAPI")
  
  private var guiComponents = List[RichWindow]();
  
  def main(args: Array[String]): Unit = {
    mainloop ! StartAPP
    
  }
  
  def shutdown {
    actorSupervisor.stop(pushapi)
    actorSupervisor.stop(mainloop)

    guiComponents.foreach( g => g.dispose)
    sys.exit
    
  }

  def setupGUI {
      val pref = new PreferencesDialog(false)
      val dialogs = Set[RichWindow](new PushLinkDialog, new NoteDialog)
      
      def toMenuItemCmd(frame:RichWindow):MenuItemCmd = { 
        (frame.title, () =>
          frame.visible match {
            case true => frame.visible = false
            case false => frame.visible = true
          })
      }
      
      val pushToDeviceName = Preferences.DefaultDeviceName
      val menuCmds = dialogs map toMenuItemCmd
      val quitCmd = {() => PushBulletApp.mainloop ! Quit }
      val prefCmd = toMenuItemCmd(pref)
      
      val tray = System.getProperty("os.name") match {
      case "Linux" => new TrayWidget(pushToDeviceName, menuCmds, quitCmd, prefCmd)
      case _ => new BulletSystemTray(dialogs, pref)
   }
      guiComponents = pref :: (dialogs.toList)
  }
}



abstract class Commands
object StartAPP extends Commands
object Quit extends Commands
case class QuitWithError(message:String) extends Commands
object API_KEY_Set extends Commands

class appLoop extends Actor {
  def receive = {
    case StartAPP if (Preferences.NeedtoConfigure) => new PreferencesDialog(true)
    case API_KEY_Set => PushBulletApp.pushapi ! GetDevices
    case StartAPP => PushBulletApp.setupGUI
    case Quit  => PushBulletApp.shutdown
    case QuitWithError(message) => PushBulletApp.shutdown //XXX add dialog with Message.

  }
}