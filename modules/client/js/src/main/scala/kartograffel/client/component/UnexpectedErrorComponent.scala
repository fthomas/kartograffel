package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object UnexpectedErrorComponent {

  val component = ScalaComponent
    .static("UnexpectedErrorComponent")(
      <.div(
        <.h4("Hoppla! Da ist wohl etwas schief gegangen...")
      )
    )
}
