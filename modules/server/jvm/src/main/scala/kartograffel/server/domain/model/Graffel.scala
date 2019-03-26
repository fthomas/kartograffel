package kartograffel.server.domain.model

import cats.Eq
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import kartograffel.shared.domain.model.Position

final case class Graffel(id: GraffelId, position: Position)

object Graffel {
  implicit val graffelEq: Eq[Graffel] =
    Eq.fromUniversalEquals

  implicit val graffelDecoder: Decoder[Graffel] =
    deriveDecoder

  implicit val graffelEncoder: Encoder[Graffel] =
    deriveEncoder
}
