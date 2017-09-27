package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object UnexpectedErrorComponent {

  case class Props(visible: Boolean)

  val component = ScalaComponent
    .builder[Props]("UnexpectedErrorComponent")
    .render_P(
      props =>
        <.div(
          if (props.visible) ^.visibility.visible else ^.visibility.hidden,
          <.h4("Hoppla! Da ist wohl etwas schief gegangen...")
      ))
    .build
}
