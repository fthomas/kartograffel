package kartograffel.server.db

import cats.Monad
import doobie.imports._
import doobie.refined._
import doobie.util.transactor.Transactor
import kartograffel.shared.model.{Entity, Graffel, Id}

trait GraffelRepository[F[_]] {
  def query(id: Id[Graffel]): F[Option[Entity[Graffel]]]

  def insert(graffel: Graffel): F[Entity[Graffel]]
}

object GraffelRepository {
  val connectionIo: GraffelRepository[ConnectionIO] =
    new GraffelRepository[ConnectionIO] {
      override def query(
          id: Id[Graffel]): ConnectionIO[Option[Entity[Graffel]]] = {
        val query: Query0[Entity[Graffel]] = sql"""
          select id, latitude, longitude from graffel where id = ${id.value}
        """.query
        query.option
      }

      override def insert(graffel: Graffel): ConnectionIO[Entity[Graffel]] = {
        val update: Update0 = sql"""
          insert into graffel (latitude, longitude)
          values (
            ${graffel.position.latitude},
            ${graffel.position.longitude}
          )
        """.update
        update
          .withUniqueGeneratedKeys[Long]("id")
          .map(Entity.from(_, graffel))
      }
    }

  def transactional[M[_]: Monad](xa: Transactor[M]): GraffelRepository[M] =
    new GraffelRepository[M] {
      override def query(id: Id[Graffel]): M[Option[Entity[Graffel]]] =
        connectionIo.query(id).transact(xa)

      override def insert(graffel: Graffel): M[Entity[Graffel]] =
        connectionIo.insert(graffel).transact(xa)
    }
}
