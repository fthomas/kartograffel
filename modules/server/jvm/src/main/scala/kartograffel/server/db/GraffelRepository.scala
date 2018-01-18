package kartograffel.server.db

import cats.implicits._
import cats.{~>, Monad}
import doobie.{Transactor, _}
import doobie.implicits._
import kartograffel.server.infrastructure.doobie.DoobieInstances
import kartograffel.server.infrastructure.doobie.statements.GraffelStatements
import kartograffel.shared.model._

trait GraffelRepository[F[_]] { self =>
  def query(id: Id[Graffel]): F[Option[Entity[Graffel]]]

  def findGraffelByPosition(position: Position): F[Option[Entity[Graffel]]]

  def insert(graffel: Graffel): F[Entity[Graffel]]

  def insert(tag: Tag): F[Entity[Tag]]

  def findTagsByPosition(pos: Position, radius: Radius): F[List[Entity[Tag]]]

  def findTagsByGraffel(id: Id[Graffel]): F[List[Entity[Tag]]]

  def findByPositionOrCreate(position: Position): F[Entity[Graffel]]

  def mapK[G[_]](t: F ~> G): GraffelRepository[G] =
    new GraffelRepository[G] {
      override def query(id: Id[Graffel]): G[Option[Entity[Graffel]]] =
        t(self.query(id))

      override def insert(graffel: Graffel): G[Entity[Graffel]] =
        t(self.insert(graffel))

      override def insert(tag: Tag): G[Entity[Tag]] =
        t(self.insert(tag))

      override def findTagsByPosition(pos: Position,
                                      radius: Radius): G[List[Entity[Tag]]] =
        t(self.findTagsByPosition(pos, radius))

      override def findGraffelByPosition(position: Position) =
        t(self.findGraffelByPosition(position))

      override def findByPositionOrCreate(position: Position) =
        t(self.findByPositionOrCreate(position))

      override def findTagsByGraffel(id: Id[Graffel]) =
        t(self.findTagsByGraffel(id))
    }
}

object GraffelRepository extends DoobieInstances {
  val connectionIo: GraffelRepository[ConnectionIO] =
    new GraffelRepository[ConnectionIO] {
      override def query(
          id: Id[Graffel]): ConnectionIO[Option[Entity[Graffel]]] =
        GraffelStatements.query(id).option

      override def insert(graffel: Graffel): ConnectionIO[Entity[Graffel]] =
        GraffelStatements
          .insert(graffel)
          .withUniqueGeneratedKeys[Id[Graffel]]("id")
          .map(Entity(_, graffel))

      override def insert(tag: Tag): ConnectionIO[Entity[Tag]] =
        GraffelStatements
          .insert(tag)
          .withUniqueGeneratedKeys[Id[Tag]]("id")
          .map(Entity(_, tag))

      override def findTagsByPosition(
          pos: Position,
          radius: Radius): ConnectionIO[List[Entity[Tag]]] =
        GraffelStatements.findTagsByPosition(pos, radius).list

      override def findGraffelByPosition(
          position: Position): ConnectionIO[Option[Entity[Graffel]]] =
        GraffelStatements.findGraffelByPosition(position).option

      override def findByPositionOrCreate(
          position: Position): ConnectionIO[Entity[Graffel]] = {
        val maybeGraffel: ConnectionIO[Option[Entity[Graffel]]] =
          findGraffelByPosition(position)

        val optF: ConnectionIO[Option[ConnectionIO[Entity[Graffel]]]] =
          maybeGraffel.map(opt => opt.map(_.pure[ConnectionIO]))

        val result: ConnectionIO[Entity[Graffel]] =
          optF.flatMap(_.getOrElse(insert(Graffel(position))))

        result
      }

      override def findTagsByGraffel(
          id: Id[Graffel]): ConnectionIO[List[Entity[Tag]]] =
        GraffelStatements.findTagsByGraffel(id).list
    }

  def transactional[M[_]: Monad](xa: Transactor[M]): GraffelRepository[M] =
    connectionIo.mapK(xa.trans)
}
