package kartograffel.server

import eu.timepit.refined.pureconfig._
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString
import fs2.Task
import kartograffel.server.Config.{Db, Http}

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

  def load: Task[Config] =
    Task.delay(pureconfig.loadConfigOrThrow[Config])
}
