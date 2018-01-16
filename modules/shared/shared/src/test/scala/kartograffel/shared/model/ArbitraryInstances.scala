package kartograffel.shared.model

import eu.timepit.refined.scalacheck.numeric._
import kartograffel.shared.domain.model.Username
import org.scalacheck.Arbitrary
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck.derive.MkArbitrary
import scala.annotation.tailrec

object ArbitraryInstances {
  implicit def arbitraryEntity[T: Arbitrary]: Arbitrary[Entity[T]] =
    MkArbitrary[Entity[T]].arbitrary

  implicit lazy val arbitraryGraffel: Arbitrary[Graffel] =
    MkArbitrary[Graffel].arbitrary

  implicit def arbitraryId[T]: Arbitrary[Id[T]] =
    MkArbitrary[Id[T]].arbitrary

  implicit lazy val arbitraryPosition: Arbitrary[Position] =
    MkArbitrary[Position].arbitrary

  implicit lazy val usernameArbitrary: Arbitrary[Username] =
    MkArbitrary[Username].arbitrary

  @tailrec
  def sampleOf[T](implicit ev: Arbitrary[T]): T =
    ev.arbitrary.sample match {
      case Some(t) => t
      case None    => sampleOf[T]
    }
}
