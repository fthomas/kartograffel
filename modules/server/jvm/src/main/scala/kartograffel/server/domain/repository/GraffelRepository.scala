package kartograffel.server.domain.repository

import cats.{~>, Monad}
import doobie._
import kartograffel.server.infrastructure.doobie.repository.DbGraffelRepository
import kartograffel.shared.domain.model.Tag
import kartograffel.shared.model._

trait GraffelRepository[F[_]] extends EntityRepository[F, Graffel] { self =>
  def findGraffelByPosition(position: Position): F[Option[Entity[Graffel]]]

  def insert(tag: Tag): F[Entity[Tag]]

  def findTagsByPosition(pos: Position, radius: Radius): F[List[Entity[Tag]]]

  def findTagsByGraffel(id: Id[Graffel]): F[List[Entity[Tag]]]

  def findByPositionOrCreate(position: Position): F[Entity[Graffel]]

  def mapK[G[_]](t: F ~> G): GraffelRepository[G] =
    new GraffelRepository[G] {
      override def findById(id: Id[Graffel]): G[Option[Entity[Graffel]]] =
        t(self.findById(id))

      override def create(graffel: Graffel): G[Entity[Graffel]] =
        t(self.create(graffel))

      override def deleteAll: G[Unit] =
        t(self.deleteAll)

      override def insert(tag: Tag): G[Entity[Tag]] =
        t(self.insert(tag))

      override def findTagsByPosition(pos: Position, radius: Radius): G[List[Entity[Tag]]] =
        t(self.findTagsByPosition(pos, radius))

      override def findGraffelByPosition(position: Position) =
        t(self.findGraffelByPosition(position))

      override def findByPositionOrCreate(position: Position) =
        t(self.findByPositionOrCreate(position))

      override def findTagsByGraffel(id: Id[Graffel]) =
        t(self.findTagsByGraffel(id))
    }
}

object GraffelRepository {
  val connectionIo: GraffelRepository[ConnectionIO] =
    DbGraffelRepository

  def transactional[M[_]: Monad](xa: Transactor[M]): GraffelRepository[M] =
    connectionIo.mapK(xa.trans)
}
