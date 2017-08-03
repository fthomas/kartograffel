package kartograffel

import eu.timepit.refined.api.RefType
import kartograffel.shared.model.Position
import kartograffel.shared.model.Position.{Latitude, Longitude}
import org.scalajs.dom
import org.scalajs.dom.{PositionOptions,window,PositionError,Coordinates}

import scala.scalajs.js

object Client {
  def main(args: Array[String]): Unit = {
    val opts = js.Object().asInstanceOf[PositionOptions]
    opts.timeout = 50000
    opts.enableHighAccuracy = true

    window.navigator.geolocation.getCurrentPosition({ pos: dom.Position =>
      println("Location" + locationFrom(pos.coords))
      println(pos)
    }, { err: PositionError =>
      println(err.code)
      println(err.message)
    }, opts)

    println("Hello, world!")
  }

  def locationFrom(coordinates: Coordinates): Option[Position] = {
    val location = for {
      latitude <- RefType.applyRef[Latitude](coordinates.latitude)
      longitude <- RefType.applyRef[Longitude](coordinates.longitude)
    } yield Position(latitude, longitude)
    location.toOption
  }
}
