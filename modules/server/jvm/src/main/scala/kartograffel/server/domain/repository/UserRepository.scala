package kartograffel.server.domain.repository

import kartograffel.shared.domain.model.{User, Username}
import kartograffel.shared.model.Entity

trait UserRepository[F[_]] extends EntityRepository[F, User] {
  def findByName(name: Username): F[Option[Entity[User]]]
}
