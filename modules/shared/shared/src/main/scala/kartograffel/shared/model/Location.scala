package kartograffel.shared.model

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Interval
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import io.circe.refined._
import kartograffel.shared.model.Location.{Latitude, Longitude}

final case class Location(
    latitude: Latitude,
    longitude: Longitude
)

object Location {
  type Latitude = Double Refined Interval.Closed[W.`-90.0`.T, W.`90.0`.T]

  type Longitude = Double Refined Interval.Closed[W.`-180.0`.T, W.`180.0`.T]

  implicit val locationDecoder: Decoder[Location] =
    deriveDecoder

  implicit val locationEncoder: Encoder[Location] =
    deriveEncoder
}
