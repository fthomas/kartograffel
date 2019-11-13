package kartograffel.client.component
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.VdomElement

object CreateTagComponent {
  final case class Props(
      value: String,
      valueChanged: ReactEventFromInput => Callback,
      createTag: Callback
  )

  final case class Backend($ : BackendScope[Props, Unit]) {
    def render(p: Props): VdomElement = <.div(
      <.label(^.`for` := "tag", "Tag name:"),
      <.input(^.id := "tag", ^.`type` := "text", ^.value := p.value, ^.onChange ==> p.valueChanged),
      <.button(^.onClick --> p.createTag, "Create Tag")
    )
  }

  private val component =
    ScalaComponent.builder[Props]("CreateTagComponent").renderBackend[Backend].build

  def apply(p: Props): Unmounted[Props, Unit, Backend] = component(p)
}
