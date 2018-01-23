package kartograffel.server.infrastructure.doobie.repository

import doobie.free.connection.ConnectionIO
import kartograffel.server.domain.repository.EntityRepository
import kartograffel.server.infrastructure.doobie.DoobieInstances
import kartograffel.server.infrastructure.doobie.statements.EntityStatements
import kartograffel.shared.model.{Entity, Id}

trait DbEntityRepository[T]
    extends EntityRepository[ConnectionIO, T]
    with DoobieInstances {

  def statements: EntityStatements[T]

  override def create(value: T): ConnectionIO[Entity[T]] =
    statements
      .create(value)
      .withUniqueGeneratedKeys[Id[T]]("id")
      .map(id => Entity(id, value))

  override def findById(id: Id[T]): ConnectionIO[Option[Entity[T]]] =
    statements.findById(id).option
}
