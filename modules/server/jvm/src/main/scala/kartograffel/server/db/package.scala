package kartograffel.server

import cats.effect.Async
import cats.syntax.functor._
import doobie._
import doobie.hikari.HikariTransactor
import eu.timepit.refined.auto._

package object db {
  def transactor[F[_]: Async](dbConfig: Config.Db): F[Transactor[F]] =
    HikariTransactor[F](
      driverClassName = dbConfig.driver,
      url = dbConfig.url,
      user = dbConfig.user,
      pass = dbConfig.password
    ).widen
}
