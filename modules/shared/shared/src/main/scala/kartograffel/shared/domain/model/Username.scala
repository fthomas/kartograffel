package kartograffel.shared.domain.model

import cats.Eq
import cats.implicits._
import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.refined._
import io.circe.{Decoder, Encoder}

final case class Username(value: NonEmptyString)

object Username {
  implicit val usernameEq: Eq[Username] =
    Eq.by(_.value)

  implicit val usernameDecoder: Decoder[Username] =
    deriveDecoder

  implicit val usernameEncoder: Encoder[Username] =
    deriveEncoder
}
