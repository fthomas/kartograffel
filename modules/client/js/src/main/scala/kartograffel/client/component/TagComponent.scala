package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import kartograffel.shared.domain.model._
import kartograffel.shared.ui.model.TagView

object TagComponent {

  case class State(tags: List[TagView] = List.empty, tagInput: String = "")

  class Backend(scope: BackendScope[Unit, State]) {
    def render(state: State): VdomElement =
      <.div(
        LeafletComponent(
          LeafletComponent.Props(400, 400, List.empty, Position(Latitude(48d), Longitude(12d)))))

  }

  private val component = ScalaComponent
    .builder[Unit]("TagComponent")
    .initialState(State())
    .renderBackend[Backend]
    .build

  def apply(): Unmounted[Unit, State, Backend] = component()
}
