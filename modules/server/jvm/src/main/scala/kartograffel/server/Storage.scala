package kartograffel.server

import fs2.interop.cats._
import doobie.imports._
import eu.timepit.refined.auto._
import fs2.Task
import org.flywaydb.core.Flyway

object Storage {
  def migrateDb(db: Config.Db): Int = {
    val flyway = new Flyway
    flyway.setDataSource(db.url.value, "", "")
    flyway.setLocations(
      s"${BuildInfo.modulePkg}.db.migration".replace('.', '/'))
    flyway.migrate()
  }

  def transactorFrom(db: Config.Db): Transactor[Task] =
    DriverManagerTransactor[Task](driver = db.driver, url = db.url)
}
