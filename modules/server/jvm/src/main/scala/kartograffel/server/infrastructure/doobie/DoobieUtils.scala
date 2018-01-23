package kartograffel.server.infrastructure.doobie

import cats.effect.Async
import doobie.hikari.HikariTransactor
import kartograffel.server.Config
import eu.timepit.refined.auto._

object DoobieUtils {
  def transactor[F[_]: Async](dbConfig: Config.Db): F[HikariTransactor[F]] =
    HikariTransactor[F](
      driverClassName = dbConfig.driver,
      url = dbConfig.url,
      user = dbConfig.user,
      pass = dbConfig.password
    )
}
