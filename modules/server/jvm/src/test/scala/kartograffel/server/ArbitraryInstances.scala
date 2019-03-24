package kartograffel.server
import java.time._

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.scalacheck.any.arbitraryFromValidate
import eu.timepit.refined.types.string.NonEmptyString
import kartograffel.server.domain.model.{Position, Radius, Tag}
import org.scalacheck.derive.MkArbitrary
import org.scalacheck.{Arbitrary, Gen}

import scala.annotation.tailrec

object ArbitraryInstances {
  implicit lazy val localDateArbitrary: Arbitrary[LocalDate] =
    Arbitrary {
      for {
        year <- Gen.chooseNum(1, 9999)
        month <- Gen.chooseNum(1, 12)
        maxDaysInMonth = Month.of(month).length(Year.of(year).isLeap)
        day <- Gen.chooseNum(1, maxDaysInMonth)
      } yield LocalDate.of(year, month, day)
    }

  implicit lazy val localTimeArbitrary: Arbitrary[LocalTime] =
    Arbitrary {
      for {
        hour <- Gen.chooseNum(0, 23)
        minute <- Gen.chooseNum(0, 59)
        second <- Gen.chooseNum(0, 59)
        nanoOfSecond <- Gen.chooseNum(0, 999999999)
      } yield LocalTime.of(hour, minute, second, nanoOfSecond)
    }

  implicit lazy val localDateTimeArbitrary: Arbitrary[LocalDateTime] =
    Arbitrary {
      for {
        date <- localDateArbitrary.arbitrary
        time <- localTimeArbitrary.arbitrary
      } yield LocalDateTime.of(date, time)
    }

  implicit lazy val nonEmptyStringArbitrary: Arbitrary[NonEmptyString] =
    arbitraryFromValidate[Refined, String, NonEmpty]

  implicit lazy val positionArbitrary: Arbitrary[Position] =
    MkArbitrary[Position].arbitrary

  implicit lazy val radiusArbitrary: Arbitrary[Radius] =
    MkArbitrary[Radius].arbitrary

  implicit lazy val tagArbitrary: Arbitrary[Tag] =
    MkArbitrary[Tag].arbitrary

  @tailrec
  def sampleOf[T](implicit ev: Arbitrary[T]): T =
    ev.arbitrary.sample match {
      case Some(t) => t
      case None    => sampleOf[T]
    }
}
