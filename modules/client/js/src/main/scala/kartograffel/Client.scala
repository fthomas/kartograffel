package kartograffel

import eu.timepit.refined.api.RefType
import kartograffel.shared.model.Location
import kartograffel.shared.model.Location.{Latitude, Longitude}

object Client {
  def main(args: Array[String]): Unit = {
    org.scalajs.dom.window.navigator.geolocation.getCurrentPosition { pos =>
      val loc = for {
        lat <- RefType.applyRef[Latitude](pos.coords.latitude)
        lon <- RefType.applyRef[Longitude](pos.coords.longitude)
      } yield Location(lat, lon)

      println("lat:" + pos.coords.latitude)
      println("lon: " + pos.coords.longitude)
      println(loc)
    }
    println("Hello, world!")
  }
}
