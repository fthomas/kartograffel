package kartograffel.server.infrastructure.doobie.repository

import doobie.ConnectionIO
import kartograffel.server.domain.repository.UserRepository
import kartograffel.server.infrastructure.doobie.DoobieInstances
import kartograffel.server.infrastructure.doobie.statements.UserStatements
import kartograffel.shared.domain.model.{User, Username}
import kartograffel.shared.model.{Entity, Id}

object DbUserRepository
    extends UserRepository[ConnectionIO]
    with DoobieInstances {

  override def create(user: User): ConnectionIO[Entity[User]] =
    UserStatements
      .create(user)
      .withUniqueGeneratedKeys[Id[User]]("id")
      .flatMap(UserStatements.findById(_).unique)

  override def deleteAll: ConnectionIO[Unit] =
    UserStatements.deleteAll.run.map(_ => ())

  override def findById(id: Id[User]): ConnectionIO[Option[Entity[User]]] =
    UserStatements.findById(id).option

  override def findByName(name: Username): ConnectionIO[Option[Entity[User]]] =
    UserStatements.findByName(name).option
}
