package kartograffel.shared.model

import cats.Eq
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Interval
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import io.circe.refined._
import kartograffel.shared.model.Position.{Latitude, Longitude}

final case class Position(
    latitude: Latitude,
    longitude: Longitude
)

object Position {
  type Latitude = Double Refined Interval.Closed[W.`-90.0`.T, W.`90.0`.T]

  type Longitude = Double Refined Interval.Closed[W.`-180.0`.T, W.`180.0`.T]

  implicit val positionEq: Eq[Position] =
    Eq.fromUniversalEquals

  implicit val positionDecoder: Decoder[Position] =
    deriveDecoder

  implicit val positionEncoder: Encoder[Position] =
    deriveEncoder
}
