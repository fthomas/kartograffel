package kartograffel.server

import eu.timepit.refined.auto._
import fs2.{Stream, Task}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp

object Server extends StreamApp {
  override def stream(args: List[String]): Stream[Task, Nothing] =
    Stream.eval(Config.load).flatMap { config =>
      blazeBuilder(config).serve
    }

  def blazeBuilder(config: Config): BlazeBuilder =
    BlazeBuilder
      .bindHttp(config.http.port, config.http.host)
      .mountService(Service.root)
      .mountService(Service.api, "/api")
      .mountService(Service.assets, "/assets")
}
