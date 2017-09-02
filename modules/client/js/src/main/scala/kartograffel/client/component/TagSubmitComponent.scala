package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import kartograffel.shared.model.Tag

object TagSubmitComponent {

  type OnChange = (ReactEventFromInput) => Callback
  type OnSubmit = (ReactEventFromInput) => Callback

  case class Props(onChange: OnChange, onSubmit: OnSubmit)

  val component = ScalaComponent
    .builder[Props]("TagSubmitComponent")
    .render_P(props =>
      <.div(
        <.input(
          ^.`type` := "text",
          ^.placeholder := "Mein Lieblingsplatz...",
          ^.onChange := props.onChange
        )
        ,
        <.button(
          ^.value := "",
          ^.onClick := props.onSubmit
        )
      )
    )
    .build
}
