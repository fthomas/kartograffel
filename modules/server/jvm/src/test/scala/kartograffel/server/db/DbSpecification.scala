package kartograffel.server.db

import doobie.specs2.imports.TaskChecker
import doobie.util.transactor.Transactor
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.refineV
import fs2.Task
import fs2.interop.cats._
import kartograffel.server.{BuildInfo, Config}
import org.specs2.mutable.Specification

trait DbSpecification extends Specification with TaskChecker {
  DbSpecification.runMigrationOnce

  override def transactor: Transactor[Task] =
    DbSpecification.transactor
}

object DbSpecification {
  val dbConfig: Config.Db = {
    val path = s"${BuildInfo.crossTarget.toString}/h2/kartograffel"
    val url = refineV[NonEmpty](
      s"jdbc:h2:$path;MODE=PostgreSQL;AUTO_SERVER=TRUE").toOption.get

    Config.Db(
      driver = "org.h2.Driver",
      url = url,
      user = "",
      password = ""
    )
  }

  lazy val runMigrationOnce: Unit =
    migrate(dbConfig).map(_ => ()).unsafeRun()

  val transactor: Transactor[Task] =
    Transactor.fromDriverManager[Task](
      driver = dbConfig.driver,
      url = dbConfig.url,
      user = dbConfig.user,
      pass = dbConfig.password
    )
}
