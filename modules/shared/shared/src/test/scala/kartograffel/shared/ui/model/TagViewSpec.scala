package kartograffel.shared.ui.model
import io.circe.testing.CodecTests
import org.scalatest.FunSuite

import io.circe.testing.instances._
import org.typelevel.discipline.scalatest.Discipline

import kartograffel.shared.SharedArbitraryInstances._

class TagViewSpec extends FunSuite with Discipline {
  checkAll("TagView", CodecTests[TagView].codec)
}
