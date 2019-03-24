package kartograffel.client.leaflet
import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.JSGlobal

@JSGlobal("L")
@js.native
object Leaflet extends js.Object {
  @js.native
  def map(id: String): Map = js.native
  @js.native
  def latLng(lat: Double, lng: Double): LatLng = js.native
  @js.native
  def tileLayer(urlTemplate: String, options: js.Dictionary[Any]): Layer = js.native
  @js.native
  def marker(latLng: LatLng): Layer = js.native

  @js.native
  trait Layer extends js.Object {
    @js.native
    def addTo(map: Map): Layer = js.native
  }

  @js.native
  trait Map extends js.Object {
    @js.native
    def setView(center: LatLng, zoom: Int): Map = js.native
  }

  @js.native
  trait LatLng extends js.Object {
    @js.native
    val lat: UndefOr[Double] = js.native
    @js.native
    val lng: UndefOr[Double] = js.native
    @js.native
    val alt: UndefOr[Double] = js.native
  }
}
