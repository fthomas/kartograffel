package kartograffel.server.infrastructure.doobie.repository
import doobie.free.connection.ConnectionIO
import kartograffel.server.domain.model.{Graffel, GraffelId}
import kartograffel.server.domain.repository.GraffelRepository
import kartograffel.server.infrastructure.doobie.statements.GraffelStatements

object DbGraffelRepository extends GraffelRepository[ConnectionIO] {
  override def findById(id: GraffelId): ConnectionIO[Option[Graffel]] =
    GraffelStatements.findById(id).option
}
