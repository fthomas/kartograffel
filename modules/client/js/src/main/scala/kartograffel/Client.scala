package kartograffel

import eu.timepit.refined.api.RefType
import kartograffel.shared.model.Location
import kartograffel.shared.model.Location.{Latitude, Longitude}
import org.scalajs.dom._

import scala.scalajs.js

object Client {
  def main(args: Array[String]): Unit = {
    println("TADA")
    window.navigator.geolocation.getCurrentPosition { pos =>
      println("huu???")
      println("Location" + locationFrom(pos.coords))
    }

    val opts = js.Object().asInstanceOf[PositionOptions]
    opts.timeout = 50000
    opts.enableHighAccuracy = true

    window.navigator.geolocation.getCurrentPosition({ pos: Position =>
      println(pos)
    }, { err: PositionError =>
      println(err.code)
      println(err.message)
    }, opts)

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
