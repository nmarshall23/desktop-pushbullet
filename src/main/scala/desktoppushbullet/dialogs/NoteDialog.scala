package desktoppushbullet.dialogs

import desktoppushbullet.PushBulletApp
import scala.swing._
import scala.swing.event.ButtonClicked
import akka.actor.actorRef2Scala
import desktoppushbullet.remoteapi.Pushable

class NoteDialog extends Frame {
  
  title = "Push Note"
  size = new Dimension(140,200)
      object pushtitle extends TextField { columns = 8 }
      object pushbody extends TextArea { columns = 10; rows = 5}
      object pushButton extends Button { text = "Push!" }

      contents = new BoxPanel(Orientation.Vertical) {
        contents += new Label { text = "Link Title" }       
        contents += pushtitle
        contents += new FlowPanel {
          contents += pushbody
          border = Swing.TitledBorder(Swing.EtchedBorder, "Note Body")
        } 
        contents += pushButton
        border = Swing.EmptyBorder(15, 10, 10, 10)
      }
      
      listenTo(pushButton)
      reactions += {
        case ButtonClicked(`pushButton`) => doPush
        
      } 
      
      def doPush {
        this.visible = false
        if((!pushtitle.text.isEmpty()) && (!pushbody.text.isEmpty())) PushBulletApp.pushapi ! Pushable.Note(pushtitle.text, pushbody.text)
      }

}