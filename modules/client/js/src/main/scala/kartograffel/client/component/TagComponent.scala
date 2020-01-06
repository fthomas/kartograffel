package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import kartograffel.client.geolocation.GeoLocation
import kartograffel.client.rest.API
import kartograffel.shared.domain.model.Position
import kartograffel.shared.ui.model.TagView
import cats.implicits._
import scala.util.{Failure, Success}

object TagComponent {
  final case class State(
      tags: List[TagView] = List.empty,
      tagInput: String = "",
      position: Option[Position] = Option.empty
  )

  final case class Backend($ : BackendScope[Unit, State]) {
    def init: Callback =
      GeoLocation.position
        .flatMap(pos => API.getGraffels(pos).map((pos, _)))
        .completeWith(res =>
          res match {
            case Success(result) =>
              $.modState(_.copy(position = Some(result._1), tags = result._2))
            case Failure(exception) => Callback(exception.printStackTrace())
          }
        )

    def render(state: State): VdomElement =
      <.div(
        ^.display.flex,
        state.position.whenDefined(p =>
          <.div(
            CreateTagComponent(
              CreateTagComponent.Props(state.tagInput, tagInputChanged, createTag)
            ),
            LeafletComponent(LeafletComponent.Props(600, 600, state.tags, p)),
            <.div(
              <.h4("Graffels in deiner Umgebung"),
              <.ul(
                state.tags.toTagMod(tv => <.li(s"Tag: ${tv.name} Position: ${tv.position}"))
              )
            )
          )
        )
      )

    private def tagInputChanged(e: ReactEventFromInput): Callback =
      e.extract(_.target.value)(value => $.modState(_.copy(tagInput = value)))

    private def createTag: Callback = $.state.flatMap { s =>
      s.position match {
        case Some(p) => sendRequest(s.tagInput, p)
        case None    => Callback.empty
      }
    }

    private def sendRequest(name: String, position: Position): Callback =
      API.createGraffel(name, position).completeWith(_ => reloadGraffels(position))

    private def reloadGraffels(p: Position) =
      API.getGraffels(p).completeWith(l => $.modState(_.copy(tags = l.toList.flatten)))
  }

  private val component = ScalaComponent
    .builder[Unit]("TagComponent")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(_.backend.init)
    .build

  def apply(): Unmounted[Unit, State, Backend] = component()
}
