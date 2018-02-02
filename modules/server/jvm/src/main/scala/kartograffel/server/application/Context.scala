package kartograffel.server.application

import cats.effect.Async
import doobie.Transactor
import fs2.Stream
import kartograffel.server.infrastructure.doobie.{DoobieMigration, DoobieUtils}

final case class Context[F[_]](
    config: Config,
    transactor: Transactor[F]
)

object Context {
  def prepare[F[_]: Async]: Stream[F, Context[F]] =
    for {
      config <- Stream.eval(Config.load[F])
      _ <- Stream.eval(DoobieMigration.run[F](config.db))
      transactor <- DoobieUtils.transactorStream[F](config.db)
    } yield Context(config, transactor)
}
