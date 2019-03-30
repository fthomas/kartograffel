package kartograffel.server.domain.model

import cats.Eq
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class Tag(id: TagId, name: String, graffelId: GraffelId)

object Tag {
  implicit val tagEq: Eq[Tag] =
    Eq.fromUniversalEquals

  implicit val tagDecoder: Decoder[Tag] =
    deriveDecoder

  implicit val tagEncoder: Encoder[Tag] =
    deriveEncoder
}
