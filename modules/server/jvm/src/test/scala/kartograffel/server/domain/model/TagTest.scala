package kartograffel.server.domain.model

import io.circe.testing.CodecTests
import io.circe.testing.instances._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

import kartograffel.server.ArbitraryInstances._

class TagTest extends FunSuite with Discipline {
  checkAll("Tag", CodecTests[Tag].codec)
}
