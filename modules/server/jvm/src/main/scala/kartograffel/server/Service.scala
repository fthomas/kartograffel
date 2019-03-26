package kartograffel.server

import java.util.UUID

import cats.effect.IO
import doobie.util.transactor.Transactor
import io.circe.syntax._
import kartograffel.server.domain.model._
import kartograffel.server.infrastructure.doobie.repository.DbGraffelRepository
import kartograffel.server.infrastructure.http4s.refined._
import kartograffel.shared.domain.model._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.headers._
import org.http4s.{HttpRoutes, MediaType}

import doobie.implicits._

object Service {

  val root: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root =>
      Ok(Html.index).map(_.withContentType(`Content-Type`(MediaType.text.html)))
  }

  object LatQueryParamMatcher extends QueryParamDecoderMatcher[Latitude]("lat")
  object LonQueryParamMatcher extends QueryParamDecoderMatcher[Longitude]("lon")

  object UUIDVar {
    def unapply(arg: String): Option[UUID] =
      try {
        Option(UUID.fromString(arg))
      } catch {
        case _: Exception => Option.empty
      }
  }

  def api(tx: Transactor[IO]): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case GET -> Root / "graffel" / UUIDVar(id) =>
        DbGraffelRepository.findById(GraffelId(id)).transact(tx).flatMap {
          case Some(entity) => Ok(entity.asJson)
          case None         => NotFound()
        }

      case GET -> Root / "version" => Ok(BuildInfo.version.asJson)
    }
}
