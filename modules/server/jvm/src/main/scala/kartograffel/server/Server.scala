package kartograffel.server

import cats.effect.IO
import eu.timepit.refined.auto._
import fs2.StreamApp.ExitCode
import fs2.{Stream, StreamApp}
import kartograffel.server.domain.repository.GraffelRepository
import kartograffel.server.infrastructure.doobie.{DoobieMigration, DoobieUtils}
import org.http4s.server.blaze.BlazeBuilder
import scala.concurrent.ExecutionContext.Implicits.global

object Server extends StreamApp[IO] {
  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    Stream.eval(prepare).flatMap(_.serve)

  def prepare: IO[BlazeBuilder[IO]] =
    for {
      config <- Config.load[IO]
      _ <- DoobieMigration.run[IO](config.db)
      xa <- DoobieUtils.transactor[IO](config.db)
      gr = GraffelRepository.transactional(xa)
    } yield blazeBuilder(config.http, gr)

  def blazeBuilder(httpConfig: Config.Http, gr: GraffelRepository[IO]): BlazeBuilder[IO] =
    BlazeBuilder[IO]
      .bindHttp(httpConfig.port, httpConfig.host)
      .mountService(Service.root)
      .mountService(Service.api(gr), "/api")
      .mountService(Service.assets, s"/${BuildInfo.assetsRoot}")
}
