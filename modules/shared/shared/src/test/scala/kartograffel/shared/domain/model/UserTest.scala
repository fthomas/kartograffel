package kartograffel.shared.domain.model

import io.circe.testing.CodecTests
import io.circe.testing.instances._
import kartograffel.shared.model.ArbitraryInstances._
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class UserTest extends FunSuite with Discipline {
  checkAll("User", CodecTests[User].unserializableCodec)
}
