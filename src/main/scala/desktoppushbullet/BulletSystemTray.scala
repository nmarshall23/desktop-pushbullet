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
import scala.swing.MainFrame

class BulletSystemTray(window:MainFrame) {
	val tray = SystemTray.getSystemTray()

	val iconFile = this.getClass.getResource("/icons/PushBullet-Icon32.png")
	//val iconImage = Toolkit.getDefaultToolkit().getImage(iconFile.filename)
	val trayIcon = new TrayIcon(ImageIO.read( iconFile))
	    
	trayIcon.addActionListener(new ActionListener() {

            def actionPerformed(e:ActionEvent) {
            	println("Hi!")
            	window.visible match {
            	  case true  => window.visible = false
            	  case false => window.visible = true
            	}
            	
            }
        });
	trayIcon.setImageAutoSize(true)
	
	tray.add(trayIcon)
	

  
}