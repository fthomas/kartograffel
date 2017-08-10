package kartograffel.server

import fs2.Task
import io.circe.syntax._
import kartograffel.shared.model.{Graffel, Id}
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.staticcontent.{webjarService, WebjarService}
import org.http4s.{HttpService, MediaType}

object Service {
  val root = HttpService {
    case GET -> Root =>
      Ok(html.index).withType(MediaType.`text/html`)
  }

  def api(gr: GraffelRepository[Task]) = HttpService {
    case GET -> Root / "graffel" / LongVar(id) =>
      gr.query(Id(id)).flatMap {
        case Some(entity) => Ok(entity.asJson)
        case None => NotFound()
      }

    case request @ POST -> Root / "graffel" =>
      request
        .as(jsonOf[Graffel])
        .flatMap(gr.insert)
        .flatMap(entity => Ok(entity.asJson))

    case GET -> Root / "version" =>
      Ok(BuildInfo.version.asJson)
  }

  val assets: HttpService =
    webjarService(WebjarService.Config())
}
