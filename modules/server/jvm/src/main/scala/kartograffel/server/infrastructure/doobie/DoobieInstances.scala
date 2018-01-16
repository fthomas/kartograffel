package kartograffel.server.infrastructure.doobie

import doobie._
import kartograffel.shared.model.Id
import eu.timepit.refined.types.numeric.NonNegLong
import scala.reflect.runtime.universe.TypeTag

trait DoobieInstances extends doobie.refined.Instances {
  implicit def idMeta[T: TypeTag]: Meta[Id[T]] =
    Meta[NonNegLong].xmap(l => Id[T](l), _.value)
}
