package kartograffel.server.db

import doobie.util.transactor.Transactor
import fs2.Task
import fs2.interop.cats._
import java.sql.Connection
import org.flywaydb.core.api.migration.jdbc.JdbcMigration

trait DoobieMigration extends JdbcMigration {
  def migrate(xa: Transactor[Task]): Task[_]

  override def migrate(connection: Connection): Unit = {
    val xa = Transactor.fromConnection[Task](connection)
    migrate(xa).unsafeRun()
    ()
  }
}
