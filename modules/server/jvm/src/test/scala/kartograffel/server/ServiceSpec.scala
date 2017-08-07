package kartograffel.server

import org.http4s._
import org.http4s.testing.Http4sMatchers
import org.specs2.mutable.Specification

object ServiceSpec extends Specification with Http4sMatchers {
  "Service.api" >> {
    "/now.json has status 200" >> {
      val request = Request(Method.GET, Uri.uri("/now.json"))
      val response = unsafeGetResponse(Service.api, request)
      response must haveStatus(Status.Ok)
    }
    "/version has MediaType application/json" >> {
      val request = Request(Method.GET, Uri.uri("/version"))
      val response = unsafeGetResponse(Service.api, request)
      response must haveMediaType(MediaType.`application/json`)
    }
  }

  "Service.assets" >> {
    "/client-opt.js contains 'Hello, world!'" >> {
      val path = s"/${BuildInfo.moduleName}/${BuildInfo.version}/client-opt.js"
      val request = Request(Method.GET, Uri(path = path))
      val response = unsafeGetResponse(Service.assets, request)
      response must haveBody(contain("Hello, world!"))
    }
  }

  def unsafeGetResponse(service: HttpService, request: Request): Response =
    service.run(request).unsafeRun().orNotFound
}
