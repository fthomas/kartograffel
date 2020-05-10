package kartograffel.shared.ui.model
import io.circe.testing.CodecTests

import io.circe.testing.instances._
import org.typelevel.discipline.scalatest.Discipline

import kartograffel.shared.SharedArbitraryInstances._
import org.scalatest.funsuite.AnyFunSuite

class TagViewSpec extends AnyFunSuite with Discipline {
  checkAll("TagView", CodecTests[TagView].codec)
}
