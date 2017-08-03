package kartograffel.shared.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

final case class Graffel(
    id: Graffel.Id,
    position: Position
)

object Graffel {
  final case class Id(value: String) extends AnyVal

  implicit val graffelIdDecoder: Decoder[Id] =
    Decoder.decodeString.map(Id.apply)

  implicit val graffelIdEncoder: Encoder[Id] =
    Encoder.encodeString.contramap(_.value)

  implicit val graffelDecoder: Decoder[Graffel] =
    deriveDecoder

  implicit val graffelEncoder: Encoder[Graffel] =
    deriveEncoder
}
