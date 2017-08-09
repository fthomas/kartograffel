package kartograffel.server

import doobie.imports._
import io.circe.syntax._
import eu.timepit.refined.auto._
import fs2.Task
import kartograffel.shared.model.Graffel.Id
import kartograffel.shared.model.{Graffel, Position}
import org.http4s.{HttpService, MediaType}
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.staticcontent.{webjarService, WebjarService}

object Service {
  val root = HttpService {
    case GET -> Root =>
      Ok(html.index).withType(MediaType.`text/html`)
  }

  def api(transactor: Transactor[Task]) = HttpService {
    case GET -> Root / "graffel" / id =>
      Ok(Graffel(Id(id), Position(0.0, 0.0)).asJson)

    case req @ POST -> Root / "post" =>
      val pos = req.as(jsonOf[Position])
      val pos1 = pos.unsafeRun()
      println(pos1)
      println(Storage)
      println(
        Storage
          .insertGraffel(Graffel(Id("0"), pos1))
          .run
          .transact(transactor)
          .unsafeRun())
      Ok("")

    case GET -> Root / "version" =>
      Ok(BuildInfo.version.asJson)
  }

  val assets: HttpService =
    webjarService(WebjarService.Config())
}
