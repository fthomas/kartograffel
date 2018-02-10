package kartograffel.server.infrastructure.http4s

import cats.effect.Effect
import kartograffel.server.BuildInfo
import org.http4s.HttpService
import org.http4s.server.staticcontent.{webjarService, WebjarService}

object Assets {
  def service[F[_]: Effect]: HttpService[F] =
    webjarService[F](WebjarService.Config())

  val prefix: String =
    s"/${BuildInfo.assetsRoot}"
}
