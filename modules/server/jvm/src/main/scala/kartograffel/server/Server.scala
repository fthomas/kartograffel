package kartograffel.server

import cats.effect.IO
import eu.timepit.refined.auto._
import fs2.Stream
import kartograffel.server.db.GraffelRepository
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.{ExitCode, StreamApp}

object Server extends StreamApp[IO] {
  override def stream(args: List[String],
                      requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    Stream.eval(prepare).flatMap(_.serve)

  def prepare: IO[BlazeBuilder[IO]] =
    for {
      config <- Config.load[IO]
      _ <- db.migrate[IO](config.db)
      xa <- db.transactor[IO](config.db)
      gr = GraffelRepository.transactional(xa)
    } yield blazeBuilder(config.http, gr)

  def blazeBuilder(httpConfig: Config.Http,
                   gr: GraffelRepository[IO]): BlazeBuilder[IO] =
    BlazeBuilder[IO]
      .bindHttp(httpConfig.port, httpConfig.host)
      .mountService(Service.root)
      .mountService(Service.api(gr), "/api")
      .mountService(Service.assets, s"/${BuildInfo.assetsRoot}")
}
