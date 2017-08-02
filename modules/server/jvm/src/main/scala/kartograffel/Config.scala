package kartograffel

import eu.timepit.refined.pureconfig._
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString
import fs2.Task

final case class Config(
    http: Http
)

final case class Http(
    host: NonEmptyString,
    port: PortNumber
)

object Config {
  def load: Task[Config] =
    Task.delay(pureconfig.loadConfigOrThrow[Config])
}
