package kartograffel.server.infrastructure.doobie.repository
import doobie.free.connection.ConnectionIO
import kartograffel.server.domain.model.{Graffel, GraffelId, Radius, Tag}
import kartograffel.server.domain.repository.TagRepository
import kartograffel.server.infrastructure.doobie.statements.TagStatements
import kartograffel.shared.domain.model.Position

object DbTagRepository extends TagRepository[ConnectionIO] {
  override def create(tag: Tag): ConnectionIO[Int] = TagStatements.create(tag).run
  override def find(name: String, graffelId: GraffelId): ConnectionIO[Option[Tag]] =
    TagStatements.find(name, graffelId).option
  override def findTagsByPosition(p: Position, r: Radius): ConnectionIO[List[(Tag, Graffel)]] =
    TagStatements.findTagsByPosition(p, r).to[List]
}
