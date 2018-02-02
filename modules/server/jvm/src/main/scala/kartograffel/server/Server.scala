package kartograffel.server

import cats.effect.IO
import eu.timepit.refined.auto._
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import kartograffel.server.application.Context
import kartograffel.server.domain.repository.GraffelRepository
import org.http4s.server.blaze.BlazeBuilder
import scala.concurrent.ExecutionContext.Implicits.global

object Server extends StreamApp[IO] {
  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    Context.prepare[IO].flatMap { ctx =>
      blazeBuilder(ctx.config.http, GraffelRepository.transactional(ctx.transactor)).serve
    }

  def blazeBuilder(httpConfig: Config.Http, gr: GraffelRepository[IO]): BlazeBuilder[IO] =
    BlazeBuilder[IO]
      .bindHttp(httpConfig.port, httpConfig.host)
      .mountService(Service.root)
      .mountService(Service.api(gr), "/api")
      .mountService(Service.assets, s"/${BuildInfo.assetsRoot}")
}
