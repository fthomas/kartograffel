package kartograffel.server

import eu.timepit.refined.auto._
import org.specs2.mutable.Specification

class ServerSpec extends Specification {
  "Server" >> {
    "call blazeBuilder" >> {
      val httpConfig = Config.Http("::", 8080)
      Server.blazeBuilder(httpConfig, null)
      true
    }
    "call stream" >> {
      Server.stream(List.empty)
      true
    }
  }
}
