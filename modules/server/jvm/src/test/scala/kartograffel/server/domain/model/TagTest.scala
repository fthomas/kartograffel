package kartograffel.server.domain.model

import io.circe.testing.CodecTests
import io.circe.testing.instances._
import org.typelevel.discipline.scalatest.Discipline

import kartograffel.server.ArbitraryInstances._
import org.scalatest.funsuite.AnyFunSuite

class TagTest extends AnyFunSuite with Discipline {
  checkAll("Tag", CodecTests[Tag].codec)
}
