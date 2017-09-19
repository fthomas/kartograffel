package kartograffel.server

import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.effect.IO
import io.circe.syntax._
import kartograffel.server.db.GraffelRepository
import kartograffel.shared.model.Position.{Latitude, Longitude}
import kartograffel.shared.model.Radius.{Length, LengthRange}
import kartograffel.shared.model._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.staticcontent.{webjarService, WebjarService}
import org.http4s._
import eu.timepit.refined._
import eu.timepit.refined.api.{RefType, Refined, Validate}

object Service {
  val root: HttpService[IO] = HttpService {
    case GET -> Root =>
      Ok(Html.index).withType(MediaType.`text/html`)
  }

  //TODO btre: make pull-request to http4s?
  implicit def refinedQueryParamDecoder[FROM, TO, F[_, _]](
      implicit queryParamDecoder: QueryParamDecoder[FROM],
      validate: Validate[FROM, TO],
      refType: RefType[F]): QueryParamDecoder[F[FROM, TO]] =
    (value: QueryParameterValue) => {
      val decoded: ValidatedNel[ParseFailure, FROM] =
        queryParamDecoder.decode(value)

      val refined: ValidatedNel[ParseFailure, Either[String, F[FROM, TO]]] =
        decoded.map(
          refType.refine[TO](_)(validate)
        )

      val withParseFailure
        : ValidatedNel[ParseFailure, Either[ParseFailure, F[FROM, TO]]] =
        refined.map(
          _.left.map(errorMsg => ParseFailure(errorMsg, errorMsg))
        )

      val withInnerNel
        : ValidatedNel[ParseFailure,
                       Either[NonEmptyList[ParseFailure], F[FROM, TO]]] =
        withParseFailure.map(_.left.map(pf => NonEmptyList(pf, Nil)))

      val outerEither: Either[NonEmptyList[ParseFailure],
                              Either[NonEmptyList[ParseFailure], F[FROM, TO]]] =
        withInnerNel.toEither

      val flattend: Either[NonEmptyList[ParseFailure], F[FROM, TO]] =
        outerEither.flatMap(identity)

      Validated.fromEither(flattend)
    }

  object LatQueryParamMatcher extends QueryParamDecoderMatcher[Latitude]("lat")
  object LonQueryParamMatcher extends QueryParamDecoderMatcher[Longitude]("lon")

  def api(gr: GraffelRepository[IO]): HttpService[IO] = HttpService {
    case GET -> Root / "graffel" / LongVar(id) =>
      // TODO use QueryParamDecoder
      gr.query(Id(Refined.unsafeApply(id))).flatMap {
        case Some(entity) => Ok(entity.asJson)
        case None         => NotFound()
      }

    case request @ POST -> Root / "graffel" =>
      request
        .as(implicitly, jsonOf[IO, Graffel])
        .flatMap(gr.insert)
        .flatMap(entity => Ok(entity.asJson))

    case GET -> Root / "graffel" / "tag" :? LatQueryParamMatcher(lat) +& LonQueryParamMatcher(
          lon) =>
      val length: Length = refineMV[LengthRange](100)
      gr.findTagsByPosition(Position(lat, lon), Radius(length, meter))
        .flatMap(tags => Ok(tags.asJson))

    case GET -> Root / "version" =>
      Ok(BuildInfo.version.asJson)
  }

  val assets: HttpService[IO] =
    webjarService(WebjarService.Config())
}
