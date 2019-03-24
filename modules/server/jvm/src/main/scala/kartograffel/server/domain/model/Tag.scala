package kartograffel.server.domain.model

import cats.Eq
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

case class Tag(name: String, graffelId: GraffelId)

object Tag {
  implicit val tagEq: Eq[Tag] =
    Eq.fromUniversalEquals

  implicit val tagDecoder: Decoder[Tag] =
    deriveDecoder

  implicit val tagEncoder: Encoder[Tag] =
    deriveEncoder
}
