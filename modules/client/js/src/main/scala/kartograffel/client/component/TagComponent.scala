package kartograffel.client.component

import japgolly.scalajs.react._
import japgolly.scalajs.react.component.builder.Lifecycle.ComponentDidMount
import japgolly.scalajs.react.vdom.html_<^._
import kartograffel.client.repository.ClientRepository
import kartograffel.shared.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object TagComponent {

  case class State(currentGraffel: Option[Entity[Graffel]],
                   tags: List[Tag],
                   tagInput: String,
                   submittingTag: Boolean,
                   unexpectedError: Option[Exception],
                   positionNotFound: Option[PositionException])

  class Backend(scope: BackendScope[Unit, State]) {
    def render(state: State): VdomElement =
      <.div(
        tagList(state),
        tagSubmit(state),
        unexpectedErrorComponent(state),
        positionNotFoundComponent(state)
      )

    private def unexpectedErrorComponent(state: State) =
      UnexpectedErrorComponent.component(
        UnexpectedErrorComponent.Props(state.unexpectedError.isDefined)
      )

    private def positionNotFoundComponent(state: State) =
      PositionNotFoundComponent.component(
        PositionNotFoundComponent.Props(state.positionNotFound.isDefined)
      )

    private def tagList(state: State) =
      TagListComponent.component(TagListComponent.Props(state.tags))

    private def tagSubmit(state: State) = {
      def onSubmit(state: State): Callback = {
        val modStateSubmittingTag =
          scope.modState(_.copy(submittingTag = true))
        val modStateFinishedSubmitting =
          scope.modState(_.copy(tagInput = "", submittingTag = false))

        def saveNewTag(): Future[List[Tag]] =
          state.currentGraffel
            .map(entity => Tag(state.tagInput, entity.id))
            .map(ClientRepository.future.saveTag)
            .getOrElse(Future.successful(Nil))

        val io = CallbackTo.future {
          (for {
            loadedTags <- saveNewTag()
          } yield {
            println(s"loaded tags: $loadedTags")
            scope.modState(_.copy(tags = loadedTags))
          }).recover {
            case e: Exception =>
              scope.modState(_.copy(unexpectedError = Some(e)))
          }
        }

        modStateSubmittingTag >> io >> modStateFinishedSubmitting
      }

      def onChange(e: ReactEventFromInput): Callback =
        e.extract(_.target.value)(value =>
          scope.modState(_.copy(tagInput = value)))

      def tagSubmitComponentEnabled =
        !state.submittingTag &&
          state.positionNotFound.isEmpty &&
          state.unexpectedError.isEmpty

      TagSubmitComponent.component(
        TagSubmitComponent.Props(
          onChange = onChange,
          onSubmit = _ => onSubmit(state),
          inputText = state.tagInput,
          enabled = tagSubmitComponentEnabled
        )
      )
    }
  }

  def loadTagsByGraffel(graffel: Option[Entity[Graffel]]): Future[List[Tag]] =
    graffel
      .map(entity => ClientRepository.future.findTags(entity.value.position))
      .getOrElse(Future.successful(Nil))

  def onComponentDidMount(
      cdm: ComponentDidMount[Unit, State, Backend]): Callback = {
    def getGraffel(): Future[Entity[Graffel]] =
      for {
        position <- ClientRepository.future.findCurrentPosition()
        graffel <- ClientRepository.future.findOrCreateGraffel(
          Graffel(position))
      } yield graffel

    def getGraffelWithTags(): Future[(Entity[Graffel], List[Tag])] =
      getGraffel().flatMap(entity =>
        loadTagsByGraffel(Some(entity)).map(tags => (entity, tags)))

    CallbackTo.future(
      getGraffelWithTags()
        .map {
          case (graffelEntity, tags) =>
            println(
              s"componentDidMount: currentGraffel=$graffelEntity, tags=$tags")
            cdm.modState(
              _.copy(currentGraffel = Some(graffelEntity), tags = tags))
        }
        .recover {
          case pe: PositionException =>
            cdm.modState(_.copy(positionNotFound = Some(pe)))
          case e: Exception => cdm.modState(_.copy(unexpectedError = Some(e)))
        }
    )
  }.void

  val component = ScalaComponent
    .builder[Unit]("TagComponent")
    .initialState(
      State(
        currentGraffel = None,
        tags = Nil,
        tagInput = "",
        submittingTag = false,
        unexpectedError = None,
        positionNotFound = None
      ))
    .renderBackend[Backend]
    .componentDidMount(onComponentDidMount)
    .build
}
