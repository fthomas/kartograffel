package kartograffel.shared.ui.model
import cats.kernel.Eq
import kartograffel.shared.domain.model.Position

final case class TagView(name: String, position: Position)

object TagView {
  implicit val tagEq: Eq[TagView] =
    Eq.fromUniversalEquals

}
