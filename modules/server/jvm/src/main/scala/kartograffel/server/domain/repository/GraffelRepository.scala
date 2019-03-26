package kartograffel.server.domain.repository
import kartograffel.server.domain.model.{Graffel, GraffelId}

trait GraffelRepository[F[_]] {
  def findById(id: GraffelId): F[Option[Graffel]]
}
