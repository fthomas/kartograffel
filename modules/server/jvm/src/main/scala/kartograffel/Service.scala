package kartograffel

import io.circe.syntax._
import eu.timepit.refined.auto._
import kartograffel.shared.model.Graffel.Id
import kartograffel.shared.model.{Graffel, Location}
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.staticcontent.{webjarService, WebjarService}

object Service {
  val api = HttpService {
    case GET -> Root / "graffel" / id =>
      Ok(Graffel(Id(""), Location(0.0, 0.0)).asJson)

    case GET -> Root / "now.json" =>
      Ok(Storage.now.map(_.asJson))

    case GET -> Root / "version.json" =>
      Ok(BuildInfo.version.asJson)
  }

  val assets: HttpService =
    webjarService(WebjarService.Config())
}
