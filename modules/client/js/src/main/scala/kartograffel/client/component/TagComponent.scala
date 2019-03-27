package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import kartograffel.client.geolocation.GeoLocation
import kartograffel.shared.domain.model.Position
import kartograffel.shared.ui.model.TagView

import scala.util.{Failure, Success}

object TagComponent {

  final case class State(tags: List[TagView] = List.empty,
                         tagInput: String = "",
                         position: Option[Position] = Option.empty)

  final case class Backend($ : BackendScope[Unit, State]) {

    def init: Callback =
      GeoLocation.position.completeWith(pos =>
        pos match {
          case Success(p)         => $.modState(_.copy(position = Some(p)))
          case Failure(exception) => Callback(exception.printStackTrace())
      })

    def render(state: State): VdomElement =
      <.div(
        state.position.whenDefined(
          p =>
            LeafletComponent(
              LeafletComponent.Props(600, 600, List.empty, p)
          ))
      )

  }

  private val component = ScalaComponent
    .builder[Unit]("TagComponent")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(_.backend.init)
    .build

  def apply(): Unmounted[Unit, State, Backend] = component()
}
