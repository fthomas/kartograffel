package kartograffel.shared.model

import cats.Eq
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import io.circe.refined._
import kartograffel.shared.domain.model.{Latitude, Longitude}

final case class Position(
    latitude: Latitude,
    longitude: Longitude
)

object Position {
  implicit val positionEq: Eq[Position] =
    Eq.fromUniversalEquals

  implicit val positionDecoder: Decoder[Position] =
    deriveDecoder

  implicit val positionEncoder: Encoder[Position] =
    deriveEncoder
}
