package kartograffel.server

import doobie.hikari.hikaritransactor.HikariTransactor
import doobie.imports._
import eu.timepit.refined.auto._
import fs2.Task
import fs2.interop.cats._
import org.flywaydb.core.Flyway

package object db {
  def migrate(dbConfig: Config.Db): Task[Int] =
    Task.delay {
      val flyway = new Flyway
      val location = classOf[migration.V0001__CreateGraffel].getPackage.getName
        .replace('.', '/')

      flyway.setDataSource(dbConfig.url, dbConfig.user, dbConfig.password)
      flyway.setLocations(location)
      flyway.migrate()
    }

  def transactor(dbConfig: Config.Db): Task[Transactor[Task]] =
    HikariTransactor[Task](
      driverClassName = dbConfig.driver,
      url = dbConfig.url,
      user = dbConfig.user,
      pass = dbConfig.password
    )
}
