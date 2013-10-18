package desktoppushbullet.systemtray

import Option.{ apply => ? }
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.widgets.TrayItem
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Listener
import org.eclipse.swt.widgets.Event
import org.eclipse.swt.widgets.Menu
import org.eclipse.swt.widgets.MenuItem

import desktoppushbullet.remoteapi._

class TrayWidget(pushToDeviceName: String, menuCmds: Set[MenuItemCmd], quitCmd: Function0[Unit], prefCmd:MenuItemCmd ) {

  private val display = new Display()
  private val shell = new Shell(display)
  private val image = new Image(display, 16, 16)

  createSystemTray

  def shutdownTray {
    shell.dispose()
    image.dispose()
    display.dispose()
  }

  private def createSystemTray {

    val tray = ?(display.getSystemTray())
    if (tray.isEmpty) {

    }

    val item = new TrayItem(tray.get, SWT.NONE)
    TrayWidget.AddTrayCallback(item, SWT.Show) { 
      () => println("Tray Showen")
    }

    val menu = createMenu
    TrayWidget.AddTrayCallback(item, SWT.MenuDetect) { 
      () => menu.setVisible(true)
    }

    item.setImage(image)

    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep()
    }

  }

  private def createMenu = {
    val menu = new Menu(shell, SWT.POP_UP)
    new MenuItem(menu, SWT.PUSH).setText(pushToDeviceName)
    new MenuItem(menu, SWT.SEPARATOR)
    menuCmds foreach TrayWidget.createMenuItems(menu)_
    new MenuItem(menu, SWT.SEPARATOR)
    TrayWidget.createMenuItems(menu)(prefCmd)
    new MenuItem(menu, SWT.SEPARATOR)
    TrayWidget.createMenuItems(menu)("Quit", quitCmd)

    menu
  }
}

object TrayWidget {
  //type MenuCmds = (String,Function0[Unit])

  private def createMenuItems(menu: Menu)(menuCmds:MenuItemCmd ) {
    val mi = new MenuItem(menu, SWT.PUSH)
    mi.setText(menuCmds._1)
    mi.addListener(SWT.Selection, new Listener() {
      def handleEvent(e: Event) {
        menuCmds._2()
      }
    })
  }

  private def AddTrayCallback(trayItem: TrayItem, eventId: Int)(cmd: Function0[Unit]) = {
    trayItem.addListener(eventId, new Listener() {
      def handleEvent(e: Event) {
        cmd()
      }
    })
  }
}
