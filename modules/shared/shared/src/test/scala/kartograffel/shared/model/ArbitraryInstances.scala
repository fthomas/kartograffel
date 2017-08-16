package kartograffel.shared.model

import eu.timepit.refined.scalacheck.numeric._
import org.scalacheck.Arbitrary
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck.derive.MkArbitrary

object ArbitraryInstances {
  implicit def arbitraryEntity[T: Arbitrary]: Arbitrary[Entity[T]] =
    MkArbitrary[Entity[T]].arbitrary

  implicit lazy val arbitraryGraffel: Arbitrary[Graffel] =
    MkArbitrary[Graffel].arbitrary

  implicit lazy val arbitraryPosition: Arbitrary[Position] =
    MkArbitrary[Position].arbitrary
}
