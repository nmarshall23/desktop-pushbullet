package desktoppushbullet

import scala.swing._
import scala.swing.event.ButtonClicked

class PreferencesDialog(needsSetup: Boolean) extends Dialog {
  title = "Prefrerences"
  object apikey extends TextField {
    columns = 5
    text = Preferences.API_KEY_Option.getOrElse("")
  }
  object saveButton extends Button { text = "Save" }
  object cancelButton extends Button { text = "Save" }

  contents = new FlowPanel {
    contents += new Label { title = "API KEY: " }
    contents += apikey
    contents += saveButton

    border = Swing.EmptyBorder(15, 10, 10, 10)
  }

  listenTo(saveButton)
  reactions += {
    case ButtonClicked(`saveButton`) => this.SaveDialog

  }

  override def defaultButton = Some(saveButton)
  
  def SaveDialog {
    Preferences.setAPI_KEY(apikey.text)
    this.visible = false
    if (needsSetup) PushBulletApp.mainloop ! API_KEY_Set
  }
  
  if(needsSetup) this.visible = true
}