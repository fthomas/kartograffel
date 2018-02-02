package kartograffel.server.infrastructure.doobie

import cats.effect.Async
import doobie.hikari.HikariTransactor
import eu.timepit.refined.auto._
import fs2.Stream
import kartograffel.server.Config

object DoobieUtils {
  def transactor[F[_]: Async](dbConfig: Config.Db): F[HikariTransactor[F]] =
    HikariTransactor.newHikariTransactor[F](
      driverClassName = dbConfig.driver,
      url = dbConfig.url,
      user = dbConfig.user,
      pass = dbConfig.password
    )

  def transactorStream[F[_]: Async](dbConfig: Config.Db): Stream[F, HikariTransactor[F]] =
    HikariTransactor.stream[F](
      driverClassName = dbConfig.driver,
      url = dbConfig.url,
      user = dbConfig.user,
      pass = dbConfig.password
    )
}
