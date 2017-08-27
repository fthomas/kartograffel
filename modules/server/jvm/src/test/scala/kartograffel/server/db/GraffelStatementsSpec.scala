package kartograffel.server.db

import doobie.specs2.imports._
import doobie.util.transactor.Transactor
import fs2.Task
import fs2.interop.cats._
import kartograffel.shared.model.Id
import org.specs2.mutable.Specification

class GraffelStatementsSpec extends Specification with TaskChecker {

  override def transactor: Transactor[Task] =
    Transactor.fromDriverManager[Task](
      driver = "org.h2.Driver",
      url = "jdbc:h2:~/.kartograffel/db/kartograffel;MODE=PostgreSQL")

  //check(GraffelStatements.query(Id(0L)))
}
