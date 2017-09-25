package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object TagSubmitComponent {

  type OnChange = (ReactEventFromInput) => Callback
  type OnSubmit = (ReactEventFromInput) => Callback

  case class Props(onChange: OnChange,
                   onSubmit: OnSubmit,
                   inputText: String,
                   enabled: Boolean)

  val component = ScalaComponent
    .builder[Props]("TagSubmitComponent")
    .render_P(props =>
      <.div(
        <.input(
          ^.`type` := "text",
          ^.placeholder := "Mein Lieblingsplatz...",
          ^.value := props.inputText,
          ^.onChange ==> props.onChange,
          ^.disabled := !props.enabled
        ),
        <.input(
          ^.value := "graffel!",
          ^.`type` := "button",
          ^.onClick ==> props.onSubmit,
          ^.disabled := !props.enabled || props.inputText.isEmpty
        )
    ))
    .build
}
