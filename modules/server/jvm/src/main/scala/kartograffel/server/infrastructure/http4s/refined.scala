package kartograffel.server.infrastructure.http4s

import cats.data.Validated.{Invalid, Valid}
import cats.implicits._
import eu.timepit.refined.api.RefinedType
import eu.timepit.refined.types.numeric.NonNegLong
import org.http4s.{ParseFailure, QueryParamDecoder, QueryParameterValue}

object refined {
  object NonNegLongVar extends RefinedPathVar[NonNegLong, Long](_.toLong)

  implicit def refinedQueryParamDecoder[F[_, _], T, P](
      implicit queryParamDecoder: QueryParamDecoder[T],
      rt: RefinedType.AuxT[F[T, P], T],
  ): QueryParamDecoder[F[T, P]] =
    (value: QueryParameterValue) => {
      queryParamDecoder.decode(value) match {
        case Valid(t) =>
          rt.refine(t).leftMap(err => ParseFailure(err, err)).toValidatedNel
        case Invalid(nel) =>
          Invalid(nel)
      }
    }
}
