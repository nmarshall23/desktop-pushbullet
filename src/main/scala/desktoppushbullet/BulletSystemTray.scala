package desktoppushbullet

import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.PopupMenu
import java.awt.MenuItem
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import javax.imageio.ImageIO
import java.awt.Image
import scala.swing.Frame
import scala.collection.immutable.Set
import scala.swing.RichWindow

class BulletSystemTray(windows: Set[RichWindow], preferencesDialog:RichWindow) {

  val tray = SystemTray.getSystemTray()
  val trayIcon = setupTrayIcon
  tray.add(trayIcon)
  
  def setupTrayIcon = {
    val iconFile = this.getClass.getResource("/icons/PushBullet-Icon32.png")
    val trayIcon = new TrayIcon(ImageIO.read(iconFile))

    val PopupMenu = setupPopupMenu()
    trayIcon.setPopupMenu(PopupMenu)
    trayIcon.setImageAutoSize(true)
    
    trayIcon
  }
  
  def setupPopupMenu():PopupMenu = {
    val popupMenu = new PopupMenu()

    val defaultDeviceLabel = Preferences.DefaultDeviceName

    popupMenu.add(defaultDeviceLabel)
    popupMenu.addSeparator()
    windows.foreach(setupDialogMenus(popupMenu)_ )
    popupMenu.addSeparator()
    setupDialogMenus(popupMenu)(preferencesDialog)
    popupMenu.addSeparator()
    setupQuitAction(popupMenu)
    
    popupMenu
  }

  def setupDialogMenus(popupMenu:PopupMenu)(frame:RichWindow) {
      val menuItem = new MenuItem(frame.title)
      menuItem.addActionListener(new ActionListener() {

        def actionPerformed(e: ActionEvent) {
          frame.visible match {
            case true => frame.visible = false
            case false => frame.visible = true
          }

        }
      });

      popupMenu.add(menuItem)
  }
  
  def setupQuitAction(popupMenu:PopupMenu) {
    val menuItem = new MenuItem("Quit")
      menuItem.addActionListener(new ActionListener() {

        def actionPerformed(e: ActionEvent) {
        	tray.remove(trayIcon)

          PushBulletApp.mainloop ! Quit
        }
      });

      popupMenu.add(menuItem)
  }

}