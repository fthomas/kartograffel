package kartograffel.server

import cats.effect.{Async, Sync}
import cats.syntax.functor._
import doobie._
import doobie.hikari.HikariTransactor
import eu.timepit.refined.auto._
import org.flywaydb.core.Flyway

package object db {
  def migrate[F[_]](dbConfig: Config.Db)(implicit F: Sync[F]): F[Int] =
    F.delay {
      val flyway = new Flyway
      val location = classOf[migration.V0001__CreateGraffel].getPackage.getName
        .replace('.', '/')

      flyway.setDataSource(dbConfig.url, dbConfig.user, dbConfig.password)
      flyway.setLocations(location)
      flyway.migrate()
    }

  def transactor[F[_]: Async](dbConfig: Config.Db): F[Transactor[F]] =
    HikariTransactor[F](
      driverClassName = dbConfig.driver,
      url = dbConfig.url,
      user = dbConfig.user,
      pass = dbConfig.password
    ).widen
}
