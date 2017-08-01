package kartograffel.shared.model

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import io.circe.refined._

final case class Location(
    latitude: Latitude,
    longitude: Longitude
)

object Location {
  implicit val locationDecoder: Decoder[Location] =
    deriveDecoder

  implicit val locationEncoder: Encoder[Location] =
    deriveEncoder
}
