package kartograffel

import java.io.FileNotFoundException
import java.nio.file.{Files, Path, Paths}
import eu.timepit.refined.auto._
import eu.timepit.refined.pureconfig._
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.types.string.NonEmptyString
import fs2.Task
import org.log4s.getLogger
import scala.util.Properties

final case class Config(
    httpHost: NonEmptyString = "::",
    httpPort: PortNumber = 8080
)

object Config {
  private val logger = getLogger

  private def propAsPath(name: String): Task[Option[Path]] =
    Task.delay(Properties.propOrNone(name).map(Paths.get(_)))

  def load(prop: String): Task[Config] =
    propAsPath(prop).flatMap {
      case None =>
        logger.info(
          s"Using default configuration (property '$prop' is not set)")
        Task.now(Config())

      case Some(path) if !Files.isReadable(path) =>
        val message =
          s"Property '$prop' references a non-accessible file: $path"
        Task.fail(new FileNotFoundException(message))

      case Some(path) =>
        logger.info(s"Loading configuration from $path")
        Task.delay(pureconfig.loadConfigOrThrow[Config](path))
    }
}
