package kartograffel.server

import io.circe.syntax._
import eu.timepit.refined.auto._
import kartograffel.BuildInfo
import kartograffel.shared.model.Graffel.Id
import kartograffel.shared.model.{Graffel, Position}
import org.http4s.{HttpService, MediaType}
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.staticcontent.{webjarService, WebjarService}

object Service {
  val root = HttpService {
    case GET -> Root =>
      Ok(s"""
          |<!DOCTYPE html>
          |<html>
          |  <head>
          |    <meta charset="UTF-8">
          |  </head>
          |  <body>
          |    <script type="text/javascript" src="assets/${BuildInfo.moduleName}/${BuildInfo.version}/client-jsdeps.js"></script>
          |    <script type="text/javascript" src="assets/${BuildInfo.moduleName}/${BuildInfo.version}/client-${BuildInfo.jsOptPostfix}.js"></script>
          |  </body>
          |</html>
          |
        """.stripMargin).withType(MediaType.`text/html`)
  }

  val api = HttpService {
    case GET -> Root / "graffel" / id =>
      Ok(Graffel(Id(id), Position(0.0, 0.0)).asJson)

    case req @ POST -> Root / "post" =>
      val pos = req.as(jsonOf[Position])
      println(pos.unsafeRun())
      Ok("")

    case GET -> Root / "now.json" =>
      Ok(Storage.now.map(_.asJson))

    case GET -> Root / "version" =>
      Ok(BuildInfo.version.asJson)
  }

  val assets: HttpService =
    webjarService(WebjarService.Config())
}
