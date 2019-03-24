package kartograffel.server.domain
import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.numeric.Interval

package object model {
  type Latitude = Double Refined Interval.Closed[W.`-90.0`.T, W.`90.0`.T]
  object Latitude extends RefinedTypeOps.Numeric[Latitude, Double]

  type Longitude = Double Refined Interval.Closed[W.`-180.0`.T, W.`180.0`.T]
  object Longitude extends RefinedTypeOps.Numeric[Longitude, Double]
}
