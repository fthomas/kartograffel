package kartograffel.server.infrastructure.doobie.repository
import doobie.free.connection.ConnectionIO
import kartograffel.server.domain.model.{Graffel, GraffelId}
import kartograffel.server.domain.repository.GraffelRepository
import kartograffel.server.infrastructure.doobie.statements.GraffelStatements
import kartograffel.shared.domain.model.Position

object DbGraffelRepository extends GraffelRepository[ConnectionIO] {
  override def findById(id: GraffelId): ConnectionIO[Option[Graffel]] =
    GraffelStatements.findById(id).option
  override def findByPosition(p: Position): ConnectionIO[Option[Graffel]] =
    GraffelStatements.findByPosition(p).option
  override def create(graffel: Graffel): ConnectionIO[Int] = GraffelStatements.create(graffel).run
}
