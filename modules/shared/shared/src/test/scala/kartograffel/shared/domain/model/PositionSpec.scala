package kartograffel.shared.domain.model

import io.circe.testing.CodecTests
import io.circe.testing.instances._
import org.typelevel.discipline.scalatest.Discipline

import kartograffel.shared.SharedArbitraryInstances._
import org.scalatest.funsuite.AnyFunSuite

class PositionSpec extends AnyFunSuite with Discipline {
  checkAll("Position", CodecTests[Position].codec)
}
