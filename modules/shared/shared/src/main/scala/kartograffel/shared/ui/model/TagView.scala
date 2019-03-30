package kartograffel.shared.ui.model
import cats.kernel.Eq
import io.circe.{Decoder, Encoder}
import kartograffel.shared.domain.model.Position
import io.circe.generic.semiauto._

final case class TagView(name: String, position: Position)

object TagView {

  implicit val tagEq: Eq[TagView] =
    Eq.fromUniversalEquals

  implicit val encoder: Encoder[TagView] = deriveEncoder
  implicit val decoder: Decoder[TagView] = deriveDecoder

}
