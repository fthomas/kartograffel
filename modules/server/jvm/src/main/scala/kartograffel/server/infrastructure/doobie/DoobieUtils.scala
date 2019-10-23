package kartograffel.server.infrastructure.doobie

import cats.effect.{Async, Blocker, ContextShift, Resource}
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import kartograffel.server.application.Config

object DoobieUtils {
  def transactor[F[_]: Async: ContextShift](dbConfig: Config.Db): Resource[F, HikariTransactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](32)
      blocker <- Blocker[F]
      xa <- HikariTransactor.newHikariTransactor[F](
        driverClassName = dbConfig.driver.value,
        url = dbConfig.url.value,
        user = dbConfig.user,
        pass = dbConfig.password,
        ce,
        blocker
      )
    } yield xa

}
