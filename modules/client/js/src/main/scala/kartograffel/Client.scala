package kartograffel

import eu.timepit.refined.api.RefType
import kartograffel.shared.model.Location
import kartograffel.shared.model.Location.{Latitude, Longitude}
import org.scalajs.dom.{window, Coordinates}

object Client {
  def main(args: Array[String]): Unit = {
    window.navigator.geolocation.getCurrentPosition { pos =>
      println(locationFrom(pos.coords))
    }
    println("Hello, world!")
  }

  def locationFrom(coordinates: Coordinates): Option[Location] = {
    val location = for {
      latitude <- RefType.applyRef[Latitude](coordinates.latitude)
      longitude <- RefType.applyRef[Longitude](coordinates.longitude)
    } yield Location(latitude, longitude)
    location.toOption
  }
}
