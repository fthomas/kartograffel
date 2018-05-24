package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import kartograffel.shared.domain.model.Tag

object TagListComponent {

  case class Props(tags: List[Tag])

  val component = ScalaComponent
    .builder[Props]("TagsComponent")
    .render_P(
      props =>
        <.div(
          props.tags.map(tag => <.span(tag.name)): _*
      ))
    .build

}
