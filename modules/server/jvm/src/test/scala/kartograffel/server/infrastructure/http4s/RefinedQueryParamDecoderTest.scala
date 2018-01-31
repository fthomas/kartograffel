package kartograffel.server.infrastructure.http4s

import cats.data.Validated
import eu.timepit.refined.types.numeric.NonNegLong
import kartograffel.server.infrastructure.http4s.refined._
import org.http4s.{ParseFailure, QueryParamDecoder, QueryParameterValue}
import org.specs2.matcher.Matchers
import org.specs2.mutable.Specification

class RefinedQueryParamDecoderTest extends Specification with Matchers {
  "QueryParamDecoder[NonNegLong](1)" >> {
    QueryParamDecoder[NonNegLong].decode(QueryParameterValue("1")) must_===
      Validated.validNel(NonNegLong(1))
  }

  "QueryParamDecoder[NonNegLong](-1)" >> {
    QueryParamDecoder[NonNegLong].decode(QueryParameterValue("-1")) must_===
      Validated.invalidNel(
        ParseFailure("Predicate (-1 < 0) did not fail.",
                     "Predicate (-1 < 0) did not fail."))
  }

  "QueryParamDecoder[NonNegLong](a)" >> {
    QueryParamDecoder[NonNegLong].decode(QueryParameterValue("a")) must_===
      Validated.invalidNel(
        ParseFailure("Query decoding Long failed", "For input string: \"a\""))
  }
}
