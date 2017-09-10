package kartograffel.client

import eu.timepit.refined.api.RefType
import io.circe.syntax._
import kartograffel.client.component.TagsComponent
import kartograffel.shared.model.Position.{Latitude, Longitude}
import kartograffel.shared.model.{Graffel, Position, Tag}
import org.scalajs.dom
import org.scalajs.dom.{Coordinates, PositionError, PositionOptions, window}
import org.scalajs.jquery.jQuery

import scala.scalajs.js
import kartograffel.client.repository.ClientRepository.task._

object Client {

  def main(args: Array[String]): Unit = {
    val program = for {
      position <- findCurrentPosition()
      tags <- findTags(position)
    } yield {
      TagsComponent
        .component(TagsComponent.Props(tags))
        .renderIntoDOM(window.document.body)
    }

    program.unsafeRunSync

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
  }

  def locationFrom(coordinates: Coordinates): Option[Position] = {
    val location = for {
      latitude <- RefType.applyRef[Latitude](coordinates.latitude)
      longitude <- RefType.applyRef[Longitude](coordinates.longitude)
    } yield Position(latitude, longitude)
    location.toOption
  }
}
