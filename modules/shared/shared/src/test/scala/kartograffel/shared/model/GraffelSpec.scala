package kartograffel.shared.model

import io.circe.testing.CodecTests
import kartograffel.shared.model.ArbitraryInstances._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class GraffelSpec extends FunSuite with Discipline {
  checkAll("Graffel", CodecTests[Graffel].codec)
}
