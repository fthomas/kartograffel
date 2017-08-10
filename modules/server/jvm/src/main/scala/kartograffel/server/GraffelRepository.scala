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
  def fromTransactor[M[_]: Monad](xa: Transactor[M]): GraffelRepository[M] =
    new GraffelRepository[M] {
      override def query(id: Id[Graffel]): M[Option[Entity[Graffel]]] =
        sql.query(id).option.transact(xa)

      override def insert(graffel: Graffel): M[Entity[Graffel]] =
        sql.insert(graffel).transact(xa)
    }

  object sql {
    def query(id: Id[Graffel]): Query0[Entity[Graffel]] =
      sql"""
        SELECT id, latitude, longitude FROM graffel
        WHERE id = ${id.value}
      """.query

    def insert(graffel: Graffel): ConnectionIO[Entity[Graffel]] =
      sql"""
        INSERT INTO graffel (latitude, longitude)
        VALUES (
          ${graffel.position.latitude},
          ${graffel.position.longitude}
        )
      """.update
        .withUniqueGeneratedKeys[Long]("id")
        .map(id => Entity.from(id, graffel))
  }
}
