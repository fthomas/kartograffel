package kartograffel.server

import cats.effect.IO
import doobie.hikari.HikariTransactor
import doobie.imports._
import eu.timepit.refined.auto._
import org.flywaydb.core.Flyway

package object db {
  def migrate(dbConfig: Config.Db): IO[Int] =
    IO {
      val flyway = new Flyway
      val location = classOf[migration.V0001__CreateGraffel].getPackage.getName
        .replace('.', '/')

      flyway.setDataSource(dbConfig.url, dbConfig.user, dbConfig.password)
      flyway.setLocations(location)
      flyway.migrate()
    }

  def transactor(dbConfig: Config.Db): IO[Transactor[IO]] =
    HikariTransactor[IO](
      driverClassName = dbConfig.driver,
      url = dbConfig.url,
      user = dbConfig.user,
      pass = dbConfig.password
    )
}
