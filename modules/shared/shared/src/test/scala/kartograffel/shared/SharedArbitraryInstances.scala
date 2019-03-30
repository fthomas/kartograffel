package kartograffel.shared
import kartograffel.shared.domain.model.Position
import org.scalacheck.Arbitrary
import org.scalacheck.derive.MkArbitrary
import eu.timepit.refined.scalacheck.numeric._

object SharedArbitraryInstances {
  implicit lazy val positionArbitrary: Arbitrary[Position] =
    MkArbitrary[Position].arbitrary
}
