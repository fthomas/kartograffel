package kartograffel.server.infrastructure.doobie.statements

import doobie.{Query0, Update0}
import kartograffel.server.infrastructure.doobie.DoobieInstances
import kartograffel.shared.model.{Entity, Id}

trait EntityStatements[T] extends DoobieInstances {
  def create(value: T): Update0

  def findById(id: Id[T]): Query0[Entity[T]]
}
