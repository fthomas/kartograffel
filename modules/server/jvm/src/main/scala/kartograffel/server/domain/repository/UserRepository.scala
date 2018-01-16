package kartograffel.server.domain.repository

import kartograffel.shared.domain.model.{User, Username}
import kartograffel.shared.model.{Entity, Id}

trait UserRepository[F[_]] {
  def findById(id: Id[User]): F[Option[Entity[User]]]

  def findByName(name: Username): F[Option[Entity[User]]]
}
