package kartograffel.server.infrastructure.http4s

import cats.effect.{ContextShift, Effect}
import kartograffel.server.BuildInfo
import org.http4s.HttpRoutes
import org.http4s.server.staticcontent.{webjarService, WebjarService}

import scala.concurrent.ExecutionContext

object Assets {
  def service[F[_]: Effect: ContextShift](executionContext: ExecutionContext): HttpRoutes[F] =
    webjarService[F](WebjarService.Config(executionContext))

  val prefix: String =
    s"/${BuildInfo.assetsRoot}"
}
