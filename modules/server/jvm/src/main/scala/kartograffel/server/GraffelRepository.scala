package kartograffel.server

import doobie.imports._
import cats.Monad
import cats.implicits._
import doobie.imports.Update0
import doobie.util.transactor.Transactor
import eu.timepit.refined.api.Refined
import kartograffel.shared.model.{Entity, Graffel, Id, Position}

trait GraffelRepository[F[_]] {
  def query(id: Id[Graffel]): F[Option[Entity[Graffel]]]

  def insert(graffel: Graffel): F[Entity[Graffel]]
}

object GraffelRepository {
  def fromTransactor[M[_]: Monad](xa: Transactor[M]): GraffelRepository[M] =
    new GraffelRepository[M] {
      override def query(id: Id[Graffel]): M[Option[Entity[Graffel]]] =
        xa.trans(sql.query(id).option)

      override def insert(graffel: Graffel): M[Entity[Graffel]] =
        xa.trans(sql.insert(graffel).run)
          .map(i => Entity.from(i.toLong, graffel))
    }

  object sql {
    def query(id: Id[Graffel]): Query0[Entity[Graffel]] =
      sql"""
        SELECT id, latitude, longitude FROM graffel
        WHERE id = ${id.value}
      """
        .query[(Long, Double, Double)]
        .map(
          x =>
            Entity(Id(x._1),
                   Graffel(Position(Refined.unsafeApply(x._2),
                                    Refined.unsafeApply(x._3)))))

    def insert(graffel: Graffel): Update0 = {
      val position = graffel.position
      sql"""
        INSERT INTO graffel (latitude, longitude)
        VALUES (${position.latitude.value}, ${position.longitude.value})
      """.update
    }
  }
}
