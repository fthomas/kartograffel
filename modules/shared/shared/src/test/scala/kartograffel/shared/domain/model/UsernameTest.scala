package kartograffel.shared.domain.model

import io.circe.testing.CodecTests
import kartograffel.shared.model.ArbitraryInstances._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class UsernameTest extends FunSuite with Discipline {
  checkAll("Username", CodecTests[Username].codec)
}
