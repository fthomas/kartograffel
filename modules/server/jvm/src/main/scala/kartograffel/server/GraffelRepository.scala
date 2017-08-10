package kartograffel.server

import doobie.imports._
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
      override def query(id: Id[Graffel]): M[Option[Entity[Graffel]]] = ???

      override def insert(graffel: Graffel): M[Entity[Graffel]] =
        xa.trans(sql.insert(graffel).run)
          .map(i => Entity.from(i.toLong, graffel))
    }

  object sql {
    def insert(graffel: Graffel): Update0 = {
      val position = graffel.position
      sql"""
        INSERT INTO graffel (latitude, longitude)
        VALUES (${position.latitude.value}, ${position.longitude.value})
      """.update
    }

  }
}
