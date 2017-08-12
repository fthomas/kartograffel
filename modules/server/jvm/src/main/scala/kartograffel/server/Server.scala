package kartograffel.server

import eu.timepit.refined.auto._
import fs2.interop.cats._
import fs2.{Stream, Task}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp

object Server extends StreamApp {
  override def stream(args: List[String]): Stream[Task, Nothing] =
    Stream.eval(Config.load).flatMap { config =>
      Storage.migrateDb(config.db).unsafeRun()
      val xa = Storage.transactorFrom(config.db)
      val gr = GraffelRepository.transactional(xa)
      blazeBuilder(config.http, gr).serve
    }

  def blazeBuilder(httpConfig: Config.Http,
                   gr: GraffelRepository[Task]): BlazeBuilder =
    BlazeBuilder
      .bindHttp(httpConfig.port, httpConfig.host)
      .mountService(Service.root)
      .mountService(Service.api(gr), "/api")
      .mountService(Service.assets, s"/${BuildInfo.assetsRoot}")
}
