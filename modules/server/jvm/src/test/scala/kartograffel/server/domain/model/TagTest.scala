package kartograffel.server.domain.model

import io.circe.testing.CodecTests
import io.circe.testing.instances._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class TagTest extends FunSuite with Discipline {
  checkAll("Tag", CodecTests[Tag].codec)
}
