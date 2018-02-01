package kartograffel.server.infrastructure.doobie.repository

import doobie.ConnectionIO
import kartograffel.server.domain.repository.UserRepository
import kartograffel.server.infrastructure.doobie.statements.UserStatements
import kartograffel.shared.domain.model.{User, Username}
import kartograffel.shared.model.Entity

object DbUserRepository extends UserRepository[ConnectionIO] with DbEntityRepository[User] {

  override def statements: UserStatements.type =
    UserStatements

  override def findByName(name: Username): ConnectionIO[Option[Entity[User]]] =
    UserStatements.findByName(name).option
}
