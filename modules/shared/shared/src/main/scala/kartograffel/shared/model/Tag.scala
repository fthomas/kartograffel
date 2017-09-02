package kartograffel.shared.model

import cats.Eq
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class Tag(name: String, graffelId: Id[Graffel])

object Tag {
  implicit val tagEq: Eq[Tag] =
    Eq.fromUniversalEquals

  implicit val tagDecoder: Decoder[Tag] =
    deriveDecoder

  implicit val tagEncoder: Encoder[Tag] =
    deriveEncoder
}
