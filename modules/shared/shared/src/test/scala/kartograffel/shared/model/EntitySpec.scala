package kartograffel.shared.model

import io.circe.testing.CodecTests
import kartograffel.shared.model.ArbitraryInstances.implicits._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class EntitySpec extends FunSuite with Discipline {
  checkAll("Entity[Graffel]", CodecTests[Entity[Graffel]].codec)
}
