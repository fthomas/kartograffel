package kartograffel.server

import doobie.imports._
import doobie.refined._
import cats.Monad
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
          SELECT id, latitude, longitude FROM graffel WHERE id = ${id.value}
        """.query
        query.option
      }

      override def insert(graffel: Graffel): ConnectionIO[Entity[Graffel]] = {
        val update: Update0 = sql"""
          INSERT INTO graffel (latitude, longitude)
          VALUES (
            ${graffel.position.latitude},
            ${graffel.position.longitude}
          )
        """.update
        update
          .withUniqueGeneratedKeys[Long]("id")
          .map(Entity.from(_, graffel))
      }
    }

  def withTransactor[M[_]: Monad](xa: Transactor[M]): GraffelRepository[M] =
    new GraffelRepository[M] {
      override def query(id: Id[Graffel]): M[Option[Entity[Graffel]]] =
        connectionIo.query(id).transact(xa)

      override def insert(graffel: Graffel): M[Entity[Graffel]] =
        connectionIo.insert(graffel).transact(xa)
    }
}
