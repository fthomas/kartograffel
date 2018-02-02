package kartograffel.server.application

import cats.effect.Sync
import eu.timepit.refined.pureconfig._
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString
import kartograffel.server.application.Config.{Db, Http}

final case class Config(
    http: Http,
    db: Db
)

object Config {
  final case class Http(
      host: NonEmptyString,
      port: PortNumber
  )

  final case class Db(
      driver: NonEmptyString,
      url: NonEmptyString,
      user: String,
      password: String
  )

  def load[F[_]](implicit F: Sync[F]): F[Config] =
    F.delay(pureconfig.loadConfigOrThrow[Config])
}
