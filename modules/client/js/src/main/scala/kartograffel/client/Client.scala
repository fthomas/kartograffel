package kartograffel.client

import cats.data.{Validated, ValidatedNel}
import cats.syntax.apply._
import eu.timepit.refined.api.RefType
import fs2.Task
import io.circe.parser._
import io.circe.syntax._
import kartograffel.client.component.TagsComponent
import kartograffel.shared.model.Position.{Latitude, Longitude}
import kartograffel.shared.model.{Graffel, Position, Tag}
import org.scalajs.dom
import org.scalajs.dom.ext._
import org.scalajs.dom.{window, Coordinates, PositionError, PositionOptions}
import org.scalajs.jquery.jQuery

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scalatags.JsDom.all._

object Client {

  implicit val scheduler: fs2.Scheduler = fs2.Scheduler.default
  implicit val strategy: fs2.Strategy = fs2.Strategy.default

  def findTags(pos: Position): Task[List[Tag]] = {
    val url = s"/api/graffel/tag?lat=${pos.latitude}&lon=${pos.longitude}"
    Task.fromFuture {
      Ajax
        .get(url)
        .map(req => decode[List[Tag]](req.responseText))
        .map(_.toTry.get)
    }
  }

  private def convertPosition(
      in: dom.Position): ValidatedNel[String, Position] = {
    val lat = in.coords.latitude
    val lon = in.coords.longitude
    val refLat: ValidatedNel[String, Latitude] = Validated
      .fromEither(
        RefType.applyRef[Latitude](lat)
      )
      .toValidatedNel
    val refLon: ValidatedNel[String, Longitude] = Validated
      .fromEither(
        RefType.applyRef[Longitude](lon)
      )
      .toValidatedNel

    (refLat, refLon).mapN(Position(_, _))
  }

  private def validationToException(
      validated: ValidatedNel[String, Position]): Position =
    validated.toEither.left
      .map(msgNel =>
        new RuntimeException(msgNel.foldLeft("")((a, b) => a + "\n" + b)))
      .toTry
      .get

  private def getCurrentPosition: Task[dom.Position] = {
    val opts = js.Object().asInstanceOf[PositionOptions]
    opts.timeout = 10000
    opts.enableHighAccuracy = true
    Task.async(
      k =>
        window.navigator.geolocation.getCurrentPosition(
          { pos: dom.Position =>
            k(Right(pos))
          }, { err: PositionError =>
            k(Left(new RuntimeException(err.message)))
          }
      ))
  }

  def findCurrentPosition(): Task[Position] =
    getCurrentPosition
      .map(convertPosition)
      .map(validationToException)

  def main(args: Array[String]): Unit = {
    val program = for {
      position <- findCurrentPosition()
      tags <- findTags(position)
    } yield {
      TagsComponent
        .component(TagsComponent.Props(tags))
        .renderIntoDOM(window.document.body)
    }

    program.unsafeRunAsync _

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
