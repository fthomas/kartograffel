package kartograffel.server.infrastructure.doobie

import cats.effect.Sync
import eu.timepit.refined.auto._
import kartograffel.server.application.Config
import org.flywaydb.core.Flyway

object DoobieMigration {
  def run[F[_]](dbConfig: Config.Db)(implicit F: Sync[F]): F[Int] =
    F.delay {
      Flyway.configure.dataSource(dbConfig.url, dbConfig.user, dbConfig.password).load().migrate()
    }
}
