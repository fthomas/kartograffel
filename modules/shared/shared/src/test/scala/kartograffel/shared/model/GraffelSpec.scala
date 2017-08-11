package kartograffel.shared.model

import eu.timepit.refined.scalacheck.numeric._
import io.circe.testing.CodecTests
import org.scalacheck.ScalacheckShapeless._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class GraffelSpec extends FunSuite with Discipline {
  checkAll("Graffel", CodecTests[Graffel].codec)
}
