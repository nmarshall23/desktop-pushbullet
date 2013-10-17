package desktoppushbullet.dialogs

import desktoppushbullet.PushBulletApp
import desktoppushbullet.pushLink
import scala.swing._
import scala.swing.event.ButtonClicked
import akka.actor.actorRef2Scala


class PushLinkDialog extends Frame {
  
  title = "Push Link"
      object linktitle extends TextField { columns = 8 }
      object linkurl extends TextField { columns = 8 }
      object pushButton extends Button { text = "Push!" }

      contents = new BoxPanel(Orientation.Vertical) {
        contents += new Label { text = "Link Title" }       
        contents += linktitle
        contents += new Label { text = "Url" }     
        contents += linkurl
        contents += pushButton
        border = Swing.EmptyBorder(15, 10, 10, 10)
      }
      
      listenTo(pushButton)
      reactions += {
        case ButtonClicked(`pushButton`) => doPush
        
      } 
      
      def doPush {
        this.visible = false
        if((!linktitle.text.isEmpty()) && (!linkurl.text.isEmpty())) PushBulletApp.pushapi ! pushLink(linktitle.text, linkurl.text)
      }
}