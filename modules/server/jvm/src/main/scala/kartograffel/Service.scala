package kartograffel

import io.circe.syntax._
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.staticcontent.{WebjarService, webjarService}

object Service {
  val api = HttpService {
    case GET -> Root / "now.json" =>
      Ok(Storage.now.map(_.asJson))

    case GET -> Root / "version.json" =>
      Ok(BuildInfo.version.asJson)
  }

  val assets: HttpService =
    webjarService(WebjarService.Config())
}
