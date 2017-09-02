package kartograffel.client.component

import kartograffel.shared.model.Tag

import japgolly.scalajs.react._

import japgolly.scalajs.react.vdom.html_<^._

object TagsComponent {

  case class Props(tags: List[Tag])

  val component = ScalaComponent
    .builder[Props]("Tags")
    .render_P(
      props =>
        <.div(
          props.tags.map(tag => <.span(tag.name)): _*
      ))
    .build

}
