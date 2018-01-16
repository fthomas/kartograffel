package kartograffel.server.infrastructure.doobie.repository

import doobie.ConnectionIO
import kartograffel.server.domain.repository.UserRepository
import kartograffel.server.infrastructure.doobie.statements.UserStatements
import kartograffel.shared.domain.model.{User, Username}
import kartograffel.shared.model.{Entity, Id}

object DbUserRepository extends UserRepository[ConnectionIO] {
  override def findById(id: Id[User]): ConnectionIO[Option[Entity[User]]] =
    UserStatements.findById(id).option

  override def findByName(name: Username): ConnectionIO[Option[Entity[User]]] =
    UserStatements.findByName(name).option
}
