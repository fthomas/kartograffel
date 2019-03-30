package kartograffel.server.domain.model
import java.util.UUID

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

final case class GraffelId(uuid: UUID)

object GraffelId {
  implicit val decoder: Decoder[GraffelId] = deriveDecoder
  implicit val encoder: Encoder[GraffelId] = deriveEncoder
}
