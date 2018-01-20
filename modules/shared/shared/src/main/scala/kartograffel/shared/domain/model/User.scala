package kartograffel.shared.domain.model

import java.time.LocalDateTime

import cats.Eq
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.java8.time._
import io.circe.{Decoder, Encoder}

final case class User(name: Username, createdAt: LocalDateTime)

object User {
  implicit val userEq: Eq[User] =
    Eq.fromUniversalEquals

  implicit val userDecoder: Decoder[User] =
    deriveDecoder

  implicit val userEncoder: Encoder[User] =
    deriveEncoder
}
