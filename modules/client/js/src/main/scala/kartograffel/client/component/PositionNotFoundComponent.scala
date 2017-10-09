package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object PositionNotFoundComponent {

  case class Props(visible: Boolean)

  val component = ScalaComponent
    .builder[Props]("PositionNotFoundComponent")
    .render_P(
      props =>
        <.div(
          if (props.visible) ^.visibility.visible else ^.visibility.collapse,
          <.h4("leider konnten wir deine Position nicht ermitteln!")
      ))
    .build
}
