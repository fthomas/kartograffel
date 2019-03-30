package kartograffel.server.domain.repository
import kartograffel.server.domain.model.{Graffel, GraffelId}
import kartograffel.shared.domain.model.Position

trait GraffelRepository[F[_]] {
  def findById(id: GraffelId): F[Option[Graffel]]
  def findByPosition(p: Position): F[Option[Graffel]]
  def create(graffel: Graffel): F[Int]
}
