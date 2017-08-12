package kartograffel.server

import doobie.imports._
import eu.timepit.refined.auto._
import fs2.Task
import fs2.interop.cats._
import kartograffel.server.db.migration.V0001__CreateGraffel
import org.flywaydb.core.Flyway

object Storage {
  def migrateDb(db: Config.Db): Task[Int] =
    Task.delay {
      val flyway = new Flyway
      val location =
        classOf[V0001__CreateGraffel].getPackage.getName.replace('.', '/')

      flyway.setDataSource(db.url, db.user, db.password)
      flyway.setLocations(location)
      flyway.migrate()
    }

  def transactorFrom(db: Config.Db): Transactor[Task] =
    Transactor.fromDriverManager(driver = db.driver,
                                 url = db.url,
                                 user = db.user,
                                 pass = db.password)
}
