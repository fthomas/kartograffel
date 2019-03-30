package kartograffel.shared.domain.model

import io.circe.testing.CodecTests
import io.circe.testing.instances._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

import kartograffel.shared.SharedArbitraryInstances._

class PositionSpec extends FunSuite with Discipline {
  checkAll("Position", CodecTests[Position].codec)
}
