package kartograffel.server.db

import cats.effect.IO
import doobie.{ConnectionIO, Transactor}
import doobie.implicits._
import java.sql.Connection
import org.flywaydb.core.api.migration.jdbc.JdbcMigration

trait DoobieMigration extends JdbcMigration {
  def migrate: ConnectionIO[_]

  override def migrate(connection: Connection): Unit = {
    val xa = Transactor.fromConnection[IO](connection)
    migrate.transact(xa).unsafeRunSync()
    ()
  }
}
