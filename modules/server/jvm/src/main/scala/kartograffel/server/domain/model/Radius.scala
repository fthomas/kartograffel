package kartograffel.server.domain.model

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Interval
import kartograffel.server.domain.model.Radius.Length

final case class Radius(length: Length, unit: DistanceUnit)

object Radius {
  type LengthRange = Interval.Closed[W.`0`.T, W.`999`.T] //if you need more define km...
  type Length = Int Refined LengthRange
}
