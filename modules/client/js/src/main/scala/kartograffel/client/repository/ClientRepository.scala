package kartograffel.client.repository

import cats.data.{Validated, ValidatedNel}
import cats.syntax.all._
import io.circe.parser._
import eu.timepit.refined.api.RefType
import fs2.Task
import io.circe.parser.decode
import kartograffel.shared.model.Position.{Latitude, Longitude}
import kartograffel.shared.model.{Entity, Position, Tag}
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.{PositionError, PositionOptions, window}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scalatags.JsDom.all._

trait ClientRepository[F[_]] { self =>

  def findTags(pos: Position): F[List[Tag]]

  def findCurrentPosition(): F[Position]

  def saveTag(tag: Tag): F[]
}

object ClientRepository {
  val task = new ClientRepository[Task] {

    implicit val scheduler: fs2.Scheduler = fs2.Scheduler.default
    implicit val strategy: fs2.Strategy = fs2.Strategy.default

    override def findTags(pos: Position): Task[List[Tag]] = {
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

      (refLat |@| refLon).map(Position(_, _))
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

    override def findCurrentPosition(): Task[Position] =
      getCurrentPosition
        .map(convertPosition)
        .map(validationToException)
  }
}
