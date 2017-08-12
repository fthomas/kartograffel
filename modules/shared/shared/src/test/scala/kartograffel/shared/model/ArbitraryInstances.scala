package kartograffel.shared.model

import eu.timepit.refined.scalacheck.numeric._
import org.scalacheck.Arbitrary
import org.scalacheck.ScalacheckShapeless._

object ArbitraryInstances {
  def arbitraryEntity[T: Arbitrary]: Arbitrary[Entity[T]] = implicitly
  val arbitraryGraffel: Arbitrary[Graffel] = implicitly
  val arbitraryPosition: Arbitrary[Position] = implicitly

  object implicits {
    implicit def arbitraryEntity[T: Arbitrary]: Arbitrary[Entity[T]] =
      ArbitraryInstances.arbitraryEntity
    implicit val arbitraryGraffel: Arbitrary[Graffel] =
      ArbitraryInstances.arbitraryGraffel
    implicit val arbitraryPosition: Arbitrary[Position] =
      ArbitraryInstances.arbitraryPosition
  }
}
