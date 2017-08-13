package kartograffel.server

import doobie.imports._
import eu.timepit.refined.auto._
import fs2.Task
import fs2.interop.cats._
import org.flywaydb.core.Flyway

package object db {
  def migrate(db: Config.Db): Task[Int] =
    Task.delay {
      val flyway = new Flyway
      val location = classOf[migration.V0001__CreateGraffel].getPackage.getName
        .replace('.', '/')

      flyway.setDataSource(db.url, db.user, db.password)
      flyway.setLocations(location)
      flyway.migrate()
    }

  def transactor(db: Config.Db): Transactor[Task] =
    Transactor.fromDriverManager(driver = db.driver,
                                 url = db.url,
                                 user = db.user,
                                 pass = db.password)
}
