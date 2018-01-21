package kartograffel.server.infrastructure.http4s

import eu.timepit.refined.types.numeric.NonNegLong
import org.specs2.matcher.Matchers
import org.specs2.mutable.Specification

class RefinedPathVarTest extends Specification with Matchers {
  "NonNegLongVar(123)" >> {
    NonNegLongVar.unapply("123") must beSome(NonNegLong(123))
  }

  "NonNegLongVar(-123)" >> {
    NonNegLongVar.unapply("-123") must beNone
  }

  "NonNegLongVar(abc)" >> {
    NonNegLongVar.unapply("abc") must beNone
  }
}
