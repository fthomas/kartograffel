package kartograffel.server.domain.service
import cats.effect.Sync
import cats.implicits._
import kartograffel.server.domain.model._
import kartograffel.server.domain.repository.{GraffelRepository, TagRepository}
import kartograffel.server.domain.util.UUIDUtil
import kartograffel.shared.domain.model.Position

class GraffelService[F[_]](graffelRepository: GraffelRepository[F],
                           tagRepository: TagRepository[F],
                           UUIDUtil: UUIDUtil[F]) {

  def create(tagName: String, position: Position)(implicit F: Sync[F]): F[Unit] =
    for {
      gOpt <- graffelRepository.findByPosition(position)
      g <- gOpt match {
        case Some(g) => g.pure[F]
        case None    => createGraffel(position)
      }
      _ <- findOrCreateTag(tagName, g.id)
    } yield ()

  def findById(graffelId: GraffelId): F[Option[Graffel]] = graffelRepository.findById(graffelId)

  def findNearPosition(position: Position, radius: Radius): F[List[(Tag, Graffel)]] =
    tagRepository.findTagsByPosition(position, radius)

  private def createGraffel(position: Position)(implicit F: Sync[F]): F[Graffel] =
    for {
      id <- UUIDUtil.uuid
      graffel = Graffel(GraffelId(id), position)
      _ <- graffelRepository.create(graffel)
    } yield graffel

  private def findOrCreateTag(name: String, graffelId: GraffelId)(implicit F: Sync[F]): F[Tag] =
    tagRepository.find(name, graffelId).flatMap {
      case Some(t) => t.pure[F]
      case None    => createTag(name, graffelId)
    }

  private def createTag(name: String, graffelId: GraffelId)(implicit F: Sync[F]): F[Tag] =
    for {
      id <- UUIDUtil.uuid
      tag = Tag(TagId(id), name, graffelId)
      _ <- tagRepository.create(tag)
    } yield tag
}
