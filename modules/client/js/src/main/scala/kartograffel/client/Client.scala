package kartograffel.client

import eu.timepit.refined.api.RefType
import io.circe.syntax._
import kartograffel.shared.model.Position.{Latitude, Longitude}
import kartograffel.shared.model.{Graffel, Position}
import org.scalajs.dom
import org.scalajs.dom.{window, Coordinates, PositionError, PositionOptions}
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import scalatags.JsDom.all._

object Client {

  val tagHtml = div(
    input(id := "tagInput",
          `type` := "text",
          placeholder := "z.B. Spielplatz, Schlagloch..."),
    input(id := "tagButton", `type` := "button", value := "Los!")
  )

  def main(args: Array[String]): Unit = {

    jQuery("body").append(tagHtml.toString)

    val opts = js.Object().asInstanceOf[PositionOptions]
    opts.timeout = 50000
    opts.enableHighAccuracy = true

    window.navigator.geolocation.getCurrentPosition(
      { pos: dom.Position =>
        val loc = locationFrom(pos.coords).get
        println("Location" + locationFrom(pos.coords))
        jQuery.post(url = "/api/graffel",
                    data = Graffel(loc).asJson.spaces2,
                    success = null,
                    dataType = null)
      }, { err: PositionError =>
        println(err.code)
        println(err.message)
      },
      opts
    )

    println("Hello, world!1111")
  }

  def locationFrom(coordinates: Coordinates): Option[Position] = {
    val location = for {
      latitude <- RefType.applyRef[Latitude](coordinates.latitude)
      longitude <- RefType.applyRef[Longitude](coordinates.longitude)
    } yield Position(latitude, longitude)
    location.toOption
  }
}
