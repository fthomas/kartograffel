package kartograffel.shared.model

import eu.timepit.refined.scalacheck.numeric._
import io.circe.testing.CodecTests
import org.scalacheck.ScalacheckShapeless._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class PositionSpec extends FunSuite with Discipline {
  checkAll("Position", CodecTests[Position].codec)
}
