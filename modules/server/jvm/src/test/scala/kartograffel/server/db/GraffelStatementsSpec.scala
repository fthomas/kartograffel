package kartograffel.server.db

import doobie.specs2.imports._
import doobie.util.transactor.Transactor
import eu.timepit.refined.auto._
import fs2.Task
import fs2.interop.cats._
import kartograffel.server.Config
import kartograffel.shared.model.ArbitraryInstances._
import kartograffel.shared.model.{Graffel, Id}
import org.specs2.mutable.Specification

class GraffelStatementsSpec extends Specification with TaskChecker {

  override def transactor: Transactor[Task] =
    Transactor.fromDriverManager[Task](
      driver = "org.h2.Driver",
      url = "jdbc:h2:~/.kartograffel/db/kartograffel;MODE=PostgreSQL")

  migrate(
    Config.Db(driver = "org.h2.Driver",
              url = "jdbc:h2:~/.kartograffel/db/kartograffel;MODE=PostgreSQL",
              user = "",
              password = "")).unsafeRun()

  check(GraffelStatements.query(sampleOf[Id[Graffel]]))
  check(GraffelStatements.insert(sampleOf[Graffel]))
}
