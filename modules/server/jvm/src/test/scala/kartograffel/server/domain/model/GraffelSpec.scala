package kartograffel.server.domain.model

import io.circe.testing.CodecTests
import io.circe.testing.instances._
import org.typelevel.discipline.scalatest.Discipline

import kartograffel.server.ArbitraryInstances._
import org.scalatest.funsuite.AnyFunSuite

class GraffelSpec extends AnyFunSuite with Discipline {
  checkAll("Graffel", CodecTests[Graffel].codec)
}
