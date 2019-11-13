package kartograffel.client

import kartograffel.client.component.TagComponent
import org.scalajs.dom.window

object Client {
  def main(args: Array[String]): Unit = {
    val _ = TagComponent().renderIntoDOM(window.document.getElementById("app"))
  }
}
