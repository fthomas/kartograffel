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

  implicit def arbitraryId[T]: Arbitrary[Id[T]] =
    MkArbitrary[Id[T]].arbitrary

  implicit lazy val arbitraryPosition: Arbitrary[Position] =
    MkArbitrary[Position].arbitrary

  def sampleOf[T](implicit ev: Arbitrary[T]): T =
    Stream.continually(ev.arbitrary.sample).flatten.head
}
