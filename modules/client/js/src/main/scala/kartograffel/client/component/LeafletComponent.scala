package kartograffel.client.component
import cats.implicits._
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.VdomElement
import japgolly.scalajs.react.vdom.html_<^._
import kartograffel.client.leaflet.Leaflet
import kartograffel.shared.model.{Graffel, Position}

import scala.scalajs.js

object LeafletComponent {
  final case class Props(width: Int,
                         height: Int,
                         graffels: List[Graffel],
                         currentPosition: Position)

  final case class Backend($ : BackendScope[Props, Unit]) {

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
  }

  private val component = ScalaComponent
    .builder[Props]("MapComponent")
    .renderBackend[Backend]
    .componentDidMount(_.backend.loadMap)
    .componentDidUpdate(f =>
      if (f.prevProps.graffels =!= f.currentProps.graffels) f.backend.loadMap else Callback.empty)
    .build

  def apply(p: Props): Unmounted[Props, Unit, Backend] = component(p)

}
