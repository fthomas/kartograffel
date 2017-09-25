package kartograffel.server

import cats.data.{NonEmptyList, Validated, ValidatedNel}
import fs2.Task
import io.circe.syntax._
import kartograffel.server.db.GraffelRepository
import kartograffel.shared.model.Position.{Latitude, Longitude}
import kartograffel.shared.model.Radius.{Length, LengthRange}
import kartograffel.shared.model._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.staticcontent.{WebjarService, webjarService}
import org.http4s._
import eu.timepit.refined._
import eu.timepit.refined.api.{RefType, Refined, Validate}
import kartograffel.server.usecase.GraffelUseCase

object Service {
  val root = HttpService {
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

  def api(gr: GraffelRepository[Task], gUC: GraffelUseCase[Task]) = HttpService {
    case GET -> Root / "graffel" / LongVar(id) =>
      // TODO use QueryParamDecoder
      gr.query(Id(Refined.unsafeApply(id))).flatMap {
        case Some(entity) => Ok(entity.asJson)
        case None         => NotFound()
      }

    case request @ PUT -> Root / "graffel" =>
      request
        .as(jsonOf[Graffel])
        .flatMap(graffel => gUC.findByPositionOrCreate(graffel.position))
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
        .as(jsonOf[Tag])
        .flatMap(gr.insert)
        .flatMap(_ => Ok())
  }

  val assets: HttpService =
    webjarService(WebjarService.Config())
}
