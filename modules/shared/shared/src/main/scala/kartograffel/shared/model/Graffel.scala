package kartograffel.shared.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

final case class Graffel(
    position: Position
)

object Graffel {
  implicit val graffelDecoder: Decoder[Graffel] =
    deriveDecoder

  implicit val graffelEncoder: Encoder[Graffel] =
    deriveEncoder
}
