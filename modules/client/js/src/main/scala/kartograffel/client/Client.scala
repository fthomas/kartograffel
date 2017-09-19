package kartograffel.client

import kartograffel.client.component.TagsComponent
import kartograffel.shared.model.Tag
import org.scalajs.dom.window
import kartograffel.client.repository.ClientRepository.future._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
      .foreach(comp => {
        window.console.info("rendering page...")
        comp.renderIntoDOM(window.document.body)
      })
  }
}
