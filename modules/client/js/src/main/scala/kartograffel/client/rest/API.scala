package kartograffel.client.rest
import io.circe.syntax._
import japgolly.scalajs.react.{AsyncCallback, Callback}
import japgolly.scalajs.react.extra.Ajax
import kartograffel.shared.domain.model.Position
import kartograffel.shared.ui.model.TagView
import org.scalajs.dom.XMLHttpRequest
import io.circe.parser._

import org.scalajs.dom.window._

import scala.util.Try

object API {

  private def origin: AsyncCallback[String] =
    AsyncCallback(
      cb =>
        Callback {
          val host = location.host
          val protocol = location.protocol
          cb(Try(s"$protocol//$host")).runNow()
        }
    )

  def createGraffel(name: String, position: Position): AsyncCallback[XMLHttpRequest] =
    origin.flatMap { baseUrl =>
      Ajax("PUT", s"$baseUrl/api/graffel")
        .send((name, position).asJson.noSpaces)
        .asAsyncCallback
    }

  def getGraffels(position: Position): AsyncCallback[List[TagView]] =
    origin.flatMap { baseUrl =>
      val req = Ajax
        .get(s"$baseUrl/api/graffels?lat=${position.latitude}&lon=${position.longitude}")
        .send
        .asAsyncCallback
      req.map(r => decode[List[TagView]](r.responseText).toOption.toList.flatten)
    }

}
