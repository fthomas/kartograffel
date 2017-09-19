package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object PositionNotFoundComponent {

  val component = ScalaComponent
    .static("PositionNotFoundComponent")(
      <.div(
        <.h4("leider konnten wir deine Position nicht ermitteln!")
      )
    )
}
