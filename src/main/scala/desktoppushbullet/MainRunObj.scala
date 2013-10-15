package desktoppushbullet

import scala.swing._
//import java.awt.
//import java.awt.{Image, MenuItem, PopupMenu, Toolkit, TrayIcon}




object FirstSwingApp extends SimpleSwingApplication {
  
  
    def top = new MainFrame {
      title = "Push Bullet"
      val radioButtons = 
      contents = new Button {
        text = "Click me"
      }
      
     // listenTo(ps)
      
    }
    
    
    val api = new PushbulletAPI("foo")
    api.getDevices
    
    this.top.visible = false 
    
    val tray = new BulletSystemTray(top)
    
  }

