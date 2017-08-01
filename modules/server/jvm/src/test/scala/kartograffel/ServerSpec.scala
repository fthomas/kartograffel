package kartograffel

import org.specs2.mutable.Specification

class ServerSpec extends Specification {
  "Server" >> {
    "call blazeBuilder" >> {
      Server.blazeBuilder(Config())
      true
    }
    "call stream" >> {
      Server.stream(List.empty)
      true
    }
  }
}
