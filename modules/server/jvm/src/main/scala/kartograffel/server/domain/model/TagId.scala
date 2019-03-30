package kartograffel.server.domain.model
import java.util.UUID

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

final case class TagId(uuid: UUID)

object TagId {
  implicit val decoder: Decoder[TagId] = deriveDecoder
  implicit val encoder: Encoder[TagId] = deriveEncoder
}
