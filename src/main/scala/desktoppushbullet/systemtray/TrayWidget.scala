package desktoppushbullet.systemtray

import Option.{ apply => ? }
import desktoppushbullet.remoteapi._
import tray.SystemTrayAdapter
import tray.SystemTrayProvider
import java.awt.PopupMenu
import java.awt.MenuItem
import java.awt.event.ActionListener
import java.awt.event.ActionEvent



class TrayWidget(pushToDeviceName: String, menuCmds: Set[MenuItemCmd], quitCmd: MenuItemCmd, prefCmd:MenuItemCmd ) {
  private val iconFile = this.getClass.getResource("/icons/PushBullet-Icon32.png")
  
  
  createSystemTray

  def shutdownTray {
    
  }

  private def createSystemTray {
    val systemTray = new SystemTrayProvider()
	val trayAdapter = systemTray.getSystemTray()
    val imageUrl = iconFile
    val tooltip = "I'm transparent under linux!" 
   val popup = createPopupMenu()
   val trayIconAdapter = trayAdapter.createAndAddTrayIcon(  
    imageUrl,   
    tooltip,  
    popup)
 

  }

  private def createPopupMenu() = {
    val popupMenu = new PopupMenu()

    

    popupMenu.add(pushToDeviceName)
    popupMenu.addSeparator()
    menuCmds foreach TrayWidget.createMenuItems(popupMenu)_ 
    popupMenu.addSeparator()
    TrayWidget.createMenuItems(popupMenu)(prefCmd)
    popupMenu.addSeparator()
    TrayWidget.createMenuItems(popupMenu)(quitCmd)

    
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
