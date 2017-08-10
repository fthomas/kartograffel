package kartograffel.server

import doobie.util.transactor.Transactor
import eu.timepit.refined.auto._
import fs2.{Stream, Task}
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp
import fs2.interop.cats._

object Server extends StreamApp {
  override def stream(args: List[String]): Stream[Task, Nothing] =
    Stream.eval(Config.load).flatMap { config =>
      val transactor = Storage.transactorFrom(config.db)
      Storage.create2(transactor)
      blazeBuilder(config, transactor).serve
    }

  def blazeBuilder(config: Config,
                   transactor: Transactor[Task]): BlazeBuilder =
    BlazeBuilder
      .bindHttp(config.http.port, config.http.host)
      .mountService(Service.root)
      .mountService(Service.api(GraffelRepository.withTransactor(transactor)),
                    "/api")
      .mountService(Service.assets, s"/${BuildInfo.assetsRoot}")
}
