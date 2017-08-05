package kartograffel.server

import eu.timepit.refined.auto._
import org.specs2.mutable.Specification

class ServerSpec extends Specification {
  "Server" >> {
    "call blazeBuilder" >> {
      Server.blazeBuilder(Config(Http("::", 8080)))
      true
    }
    "call stream" >> {
      Server.stream(List.empty)
      true
    }
  }
}
