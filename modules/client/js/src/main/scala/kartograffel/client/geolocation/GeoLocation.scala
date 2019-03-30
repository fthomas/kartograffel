package kartograffel.client.geolocation
import japgolly.scalajs.react.{AsyncCallback, Callback}
import kartograffel.shared.domain.model.{Latitude, Longitude, Position}
import org.scalajs.dom.window._

import scala.util.Try

object GeoLocation {

  type CallbackType = Try[Position] => Callback

  val position: AsyncCallback[Position] = AsyncCallback { cb: CallbackType =>
    Callback(navigator.geolocation.getCurrentPosition { sCb =>
      val latitude = sCb.coords.latitude
      val longitude = sCb.coords.longitude

      val position = for {
        lat <- Latitude.from(latitude)
        long <- Longitude.from(longitude)
      } yield Position(lat, long)

      cb(position.left.map(s => new Throwable(s)).toTry).runNow()
    })
  }

}
