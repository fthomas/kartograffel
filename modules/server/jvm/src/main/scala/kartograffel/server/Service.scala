package kartograffel.server

import cats.effect.IO
import eu.timepit.refined._
import io.circe.syntax._
import kartograffel.server.domain.repository.GraffelRepository
import kartograffel.server.infrastructure.http4s.refined._
import kartograffel.shared.domain.model.{Latitude, Longitude}
import kartograffel.shared.model.Radius.{Length, LengthRange}
import kartograffel.shared.model._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.staticcontent.{webjarService, WebjarService}

object Service {
  val dsl = Http4sDsl[IO]
  import dsl._

  val root: HttpService[IO] = HttpService {
    case GET -> Root =>
      Ok(Html.index).map(_.withType(MediaType.`text/html`))
  }

  object LatQueryParamMatcher extends QueryParamDecoderMatcher[Latitude]("lat")
  object LonQueryParamMatcher extends QueryParamDecoderMatcher[Longitude]("lon")

  def api(gr: GraffelRepository[IO]): HttpService[IO] =
    HttpService {
      case GET -> Root / "graffel" / NonNegLongVar(id) =>
        gr.findById(Id(id)).flatMap {
          case Some(entity) => Ok(entity.asJson)
          case None         => NotFound()
        }

      case request @ PUT -> Root / "graffel" =>
        request
          .as(implicitly, jsonOf[IO, Graffel])
          .flatMap(graffel => gr.findByPositionOrCreate(graffel.position))
          .flatMap(entity => Ok(entity.asJson))

      case GET -> Root / "graffel" / "tag" :? LatQueryParamMatcher(lat) +& LonQueryParamMatcher(
            lon) =>
        val length: Length = refineMV[LengthRange](100)
        gr.findTagsByPosition(Position(lat, lon), Radius(length, meter))
          .map(_.map(_.value))
          .flatMap(tags => Ok(tags.asJson))

      case GET -> Root / "version" =>
        Ok(BuildInfo.version.asJson)

      case request @ POST -> Root / "graffel" / "tag" =>
        request
          .as(implicitly, jsonOf[IO, Tag])
          .flatMap(gr.insert)
          .flatMap(
            newTag =>
              gr.findTagsByGraffel(newTag.value.graffelId)
                .flatMap(tagEntities => Ok(tagEntities.map(_.value).asJson)))
    }

  val assets: HttpService[IO] =
    webjarService(WebjarService.Config())
}
