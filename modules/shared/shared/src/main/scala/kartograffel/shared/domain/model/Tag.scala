package kartograffel.shared.domain.model

import cats.Eq
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import kartograffel.shared.model.{Graffel, Id}

case class Tag(name: String, graffelId: Id[Graffel])

object Tag {
  implicit val tagEq: Eq[Tag] =
    Eq.fromUniversalEquals

  implicit val tagDecoder: Decoder[Tag] =
    deriveDecoder

  implicit val tagEncoder: Encoder[Tag] =
    deriveEncoder
}
