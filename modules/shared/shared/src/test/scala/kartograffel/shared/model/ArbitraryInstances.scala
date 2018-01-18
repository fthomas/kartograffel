package kartograffel.shared.model

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.scalacheck.any.arbitraryFromValidate
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.types.string.NonEmptyString
import kartograffel.shared.domain.model.{User, Username}
import org.scalacheck.Arbitrary
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck.derive.MkArbitrary

import scala.annotation.tailrec

object ArbitraryInstances {
  implicit def entityArbitrary[T: Arbitrary]: Arbitrary[Entity[T]] =
    MkArbitrary[Entity[T]].arbitrary

  implicit lazy val graffelArbitrary: Arbitrary[Graffel] =
    MkArbitrary[Graffel].arbitrary

  implicit def idArbitrary[T]: Arbitrary[Id[T]] =
    MkArbitrary[Id[T]].arbitrary

  implicit lazy val nonEmptyStringArbitrary: Arbitrary[NonEmptyString] =
    arbitraryFromValidate[Refined, String, NonEmpty]

  implicit lazy val positionArbitrary: Arbitrary[Position] =
    MkArbitrary[Position].arbitrary

  implicit lazy val radiusArbitrary: Arbitrary[Radius] =
    MkArbitrary[Radius].arbitrary

  implicit lazy val tagArbitrary: Arbitrary[Tag] =
    MkArbitrary[Tag].arbitrary

  implicit lazy val userArbitrary: Arbitrary[User] =
    MkArbitrary[User].arbitrary

  implicit lazy val usernameArbitrary: Arbitrary[Username] =
    MkArbitrary[Username].arbitrary

  @tailrec
  def sampleOf[T](implicit ev: Arbitrary[T]): T =
    ev.arbitrary.sample match {
      case Some(t) => t
      case None    => sampleOf[T]
    }
}
