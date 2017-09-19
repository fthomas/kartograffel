package kartograffel.client

import kartograffel.client.component.{
  PositionNotFoundComponent,
  TagsComponent,
  UnexpectedErrorComponent
}
import kartograffel.shared.model.Tag
import org.scalajs.dom.window
import kartograffel.client.repository.ClientRepository.future._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Client {

  def main(args: Array[String]): Unit = {

    val tags: Future[List[Tag]] = for {
      position <- findCurrentPosition()
      tags <- findTags(position)
    } yield {
      tags
    }

    val component = tags.map { tags =>
      TagsComponent
        .component(TagsComponent.Props(tags))
    }

    component
      .onComplete {
        case Success(comp) => comp.renderIntoDOM(window.document.body)
        case Failure(error) =>
          error match {
            case _: PositionException =>
              PositionNotFoundComponent
                .component()
                .renderIntoDOM(window.document.body)
            case error: Throwable =>
              error.printStackTrace()
              UnexpectedErrorComponent
                .component()
                .renderIntoDOM(window.document.body)
          }
      }
  }
}
