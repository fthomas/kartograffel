package kartograffel.shared.model

final case class Entity[T](id: Id[T], value: T)

object Entity {
  def from[T](id: Long, value: T): Entity[T] =
    Entity(Id(id), value)
}

final case class Id[T](value: Long) extends AnyVal
