package kartograffel.shared.model

import eu.timepit.refined.scalacheck.numeric._
import io.circe.testing.CodecTests
import org.scalacheck.ScalacheckShapeless._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class EntitySpec extends FunSuite with Discipline {
  checkAll("Entity[Graffel]", CodecTests[Entity[Graffel]].codec)
}
