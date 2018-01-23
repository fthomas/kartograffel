package kartograffel.server.domain.repository

import kartograffel.shared.model.{Entity, Id}

trait EntityRepository[F[_], T] {
  def create(value: T): F[Entity[T]]

  def findById(id: Id[T]): F[Option[Entity[T]]]
}
