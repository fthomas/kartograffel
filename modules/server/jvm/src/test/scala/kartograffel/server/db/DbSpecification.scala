package kartograffel.server.db

import doobie.specs2.imports.TaskChecker
import doobie.util.transactor.Transactor
import eu.timepit.refined.auto._
import fs2.Task
import fs2.interop.cats._
import kartograffel.server.Config
import org.specs2.mutable.Specification

trait DbSpecification extends Specification with TaskChecker {
  locally(DbSpecification.scriptsExecuted)

  override def transactor: Transactor[Task] =
    Transactor.fromDriverManager[Task](
      driver = "org.h2.Driver",
      url = "jdbc:h2:~/.kartograffel/db/kartograffel;MODE=PostgreSQL")
}

object DbSpecification {
  val dbConfig =
    Config.Db(driver = "org.h2.Driver",
              url = "jdbc:h2:~/.kartograffel/db/kartograffel;MODE=PostgreSQL",
              user = "",
              password = "")

  val scriptsExecuted: Int =
    migrate(dbConfig).unsafeRun()
}
