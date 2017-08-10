package kartograffel.server

import doobie.imports._
import doobie.refined._
import cats.Monad
import cats.implicits._
import doobie.imports.Update0
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
        sql
          .insert(graffel)
          .run
          .transact(xa)
          .map(i => Entity.from(i.toLong, graffel))
    }

  object sql {
    def query(id: Id[Graffel]): Query0[Entity[Graffel]] =
      sql"""
        SELECT id, latitude, longitude FROM graffel
        WHERE id = ${id.value}
      """.query

    def insert(graffel: Graffel): Update0 = {
      val position = graffel.position
      sql"""
        INSERT INTO graffel (latitude, longitude)
        VALUES (${position.latitude}, ${position.longitude})
      """.update
    }
  }
}
