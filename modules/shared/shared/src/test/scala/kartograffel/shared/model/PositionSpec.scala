package kartograffel.shared.model

import io.circe.testing.CodecTests
import kartograffel.shared.model.ArbitraryInstances.implicits._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class PositionSpec extends FunSuite with Discipline {
  checkAll("Position", CodecTests[Position].codec)
}
