package kartograffel.server.db

import doobie.imports._
import fs2.Task
import fs2.interop.cats._
import java.sql.Connection
import org.flywaydb.core.api.migration.jdbc.JdbcMigration

trait DoobieMigration extends JdbcMigration {
  def migrate: ConnectionIO[_]

  override def migrate(connection: Connection): Unit = {
    val xa = Transactor.fromConnection[Task](connection)
    migrate.transact(xa).unsafeRun()
    ()
  }
}
