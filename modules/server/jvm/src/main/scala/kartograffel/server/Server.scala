package kartograffel.server

import eu.timepit.refined.auto._
import fs2.interop.cats._
import fs2.{Stream, Task}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp

object Server extends StreamApp {
  override def stream(args: List[String]): Stream[Task, Nothing] =
    Stream.eval(prepare).flatMap(_.serve)

  def prepare: Task[BlazeBuilder] =
    for {
      config <- Config.load
      _ <- db.migrate(config.db)
      xa = db.transactor(config.db)
      gr = GraffelRepository.transactional(xa)
    } yield blazeBuilder(config.http, gr)

  def blazeBuilder(httpConfig: Config.Http,
                   gr: GraffelRepository[Task]): BlazeBuilder =
    BlazeBuilder
      .bindHttp(httpConfig.port, httpConfig.host)
      .mountService(Service.root)
      .mountService(Service.api(gr), "/api")
      .mountService(Service.assets, s"/${BuildInfo.assetsRoot}")
}
