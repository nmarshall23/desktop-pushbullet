package desktoppushbullet.systemtray

import Option.{ apply => ? }
import desktoppushbullet.remoteapi._
import java.awt.PopupMenu
import java.awt.MenuItem
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.SystemTray
import java.awt.TrayIcon
import javax.imageio.ImageIO
import java.awt.event.MouseListener
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.awt.dnd.DropTarget



class TrayWidget(pushToDeviceName: String, menuCmds: Set[MenuItemCmd], quitCmd: MenuItemCmd, prefCmd:MenuItemCmd ) {
  private val iconFile = this.getClass.getResource("/icons/PushBullet-Icon32.png")
  
  val tray = SystemTray.getSystemTray()
  val trayIcon = createSystemTray
  tray.add(trayIcon)
  
  

  def shutdownTray {
    tray.remove(trayIcon)
  }

  private def createSystemTray = {
    val trayIcon = new TrayIcon(ImageIO.read(iconFile))

    val PopupMenu = createPopupMenu()
    trayIcon.setPopupMenu(PopupMenu)
    trayIcon.setImageAutoSize(true)
    trayIcon.addMouseListener(new MouseListener() {
      def mouseClicked(e:MouseEvent) {
        println("mouse Clicked:" + e.getButton() )
      }
      
      def mouseEntered(e:MouseEvent){}
      def mouseExited(e:MouseEvent){}
      def mousePressed(e:MouseEvent){}
      def mouseReleased(e:MouseEvent){}
    })
    
    
    
    trayIcon
 

  }

  private def createPopupMenu() = {
    val popupMenu = new PopupMenu()

    

    popupMenu.add(pushToDeviceName)
    popupMenu.addSeparator()
    menuCmds foreach TrayWidget.createMenuItems(popupMenu)_ 
    popupMenu.addSeparator()
    TrayWidget.createMenuItems(popupMenu)(prefCmd)
    popupMenu.addSeparator()
    val nQuitCmd = compositeMenuItemCmd(quitCmd){(Unit) => this.shutdownTray }
    TrayWidget.createMenuItems(popupMenu)(nQuitCmd)

    
    popupMenu
  }
}

object TrayWidget {

  private def createMenuItems(popupMenu: PopupMenu)(menuCmds:MenuItemCmd ) {
    val mi = new MenuItem(menuCmds._1)
    mi.addActionListener(new ActionListener() {

      def actionPerformed(e: ActionEvent) {
          menuCmds._2()
        }
      })

      popupMenu.add(mi)
  }

}
