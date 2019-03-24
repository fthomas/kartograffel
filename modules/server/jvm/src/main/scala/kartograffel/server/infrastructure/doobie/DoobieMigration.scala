package kartograffel.server.infrastructure.doobie

import java.sql.Connection

import cats.effect.{IO, Sync}
import doobie._
import doobie.implicits._
import eu.timepit.refined.auto._
import kartograffel.server.application.Config
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.migration.jdbc.JdbcMigration

trait DoobieMigration extends JdbcMigration {
  def migrate: ConnectionIO[_]

  override def migrate(connection: Connection): Unit = {
    val xa = Transactor.fromConnection[IO](connection)
    migrate.transact(xa).unsafeRunSync()
    ()
  }
}

object DoobieMigration {
  def run[F[_]](dbConfig: Config.Db)(implicit F: Sync[F]): F[Int] =
    F.delay {
      val flyway = new Flyway
      flyway.setDataSource(dbConfig.url, dbConfig.user, dbConfig.password)
      flyway.migrate()
    }
}
