package kartograffel.shared.model

import io.circe.testing.CodecTests
import io.circe.testing.instances._
import kartograffel.shared.domain.model.Tag
import kartograffel.shared.model.ArbitraryInstances._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class TagTest extends FunSuite with Discipline {
  checkAll("Tag", CodecTests[Tag].codec)
}
