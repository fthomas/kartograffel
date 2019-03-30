package kartograffel.server.domain.repository
import kartograffel.server.domain.model.{Graffel, GraffelId, Radius, Tag}
import kartograffel.shared.domain.model.Position

trait TagRepository[F[_]] {
  def create(tag: Tag): F[Int]
  def find(name: String, graffelId: GraffelId): F[Option[Tag]]
  def findTagsByPosition(p: Position, r: Radius): F[List[(Tag, Graffel)]]
}
