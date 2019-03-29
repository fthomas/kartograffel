package kartograffel.client.component
import cats.implicits._
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import kartograffel.client.leaflet.Leaflet
import kartograffel.client.leaflet.Leaflet.Layer
import kartograffel.shared.domain.model.Position
import kartograffel.shared.ui.model.TagView
import japgolly.scalajs.react.

import scala.scalajs.js

object LeafletComponent {
  final case class Props(width: Int,
                         height: Int,
                         graffels: List[TagView],
                         currentPosition: Position)

  final case class State(m: Option[Leaflet.Map] = Option.empty, ls: List[Layer] = List.empty)

  final case class Backend($ : BackendScope[Props, State]) {

    def render(p: Props): VdomElement =
      <.div(^.id := "myMap", ^.width := p.width.px, ^.height := p.height.px)

    def loadMap: Callback = $.props.flatMap { p =>
      renderMap(p)
    }

    def renderMap(p: Props): Callback = Callback {
      val map = Leaflet
        .map("myMap")
        .setView(
          Leaflet.latLng(p.currentPosition.latitude.value, p.currentPosition.longitude.value),
          13)

      Leaflet
        .tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", js.Dictionary[Any]())
        .addTo(map)

      Leaflet
        .marker(Leaflet.latLng(p.currentPosition.latitude.value, p.currentPosition.longitude.value))
        .addTo(map)

      p.graffels.foreach(
        g =>
          Leaflet
            .marker(Leaflet.latLng(g.position.latitude.value, g.position.longitude.value))
            .addTo(map))
    }

    def updateMap(p: Props) = $.state.flatMap { s =>
      s.m.traverse(map => removeLayer(map, s.ls) >> addLayer(map, p.graffels))

      ???
    }

    def removeLayer(m: Leaflet.Map, ls: List[Layer]): Callback = Callback(ls.foreach(m.removeLayer))

    def addLayer(m: Leaflet.Map, tags: List[TagView]): CallbackTo[List[Layer]] =
      CallbackTo(
        tags.map(
          g =>
            Leaflet
              .marker(Leaflet.latLng(g.position.latitude.value, g.position.longitude.value))
              .addTo(m)))

  }

  private val component = ScalaComponent
    .builder[Props]("MapComponent")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(_.backend.loadMap)
    .componentDidUpdate(f =>
      if (f.prevProps.graffels =!= f.currentProps.graffels || f.prevProps.currentPosition =!= f.currentProps.currentPosition)
        f.backend.loadMap
      else Callback.empty)
    .build

  def apply(p: Props): Unmounted[Props, State, Backend] = component(p)

}
