package kartograffel.server.db

import cats.{~>, Monad}
import doobie.imports._
import doobie.refined._
import doobie.util.transactor.Transactor
import kartograffel.shared.model._

trait GraffelRepository[F[_]] { self =>
  def query(id: Id[Graffel]): F[Option[Entity[Graffel]]]

  def insert(graffel: Graffel): F[Entity[Graffel]]

  def insert(tag: Tag): F[Entity[Graffel]]

  def findTagsByPosition(pos: Position, radius: Radius): F[List[Entity[Tag]]]

  def mapK[G[_]](t: F ~> G): GraffelRepository[G] =
    new GraffelRepository[G] {
      override def query(id: Id[Graffel]): G[Option[Entity[Graffel]]] =
        t(self.query(id))

      override def insert(graffel: Graffel): G[Entity[Graffel]] =
        t(self.insert(graffel))

      override def insert(tag: Tag): G[Entity[Graffel]] =
        t(self.insert(tag))

      override def findTagsByPosition(pos: Position,
                                      radius: Radius): G[List[Entity[Tag]]] =
        t(self.findTagsByPosition(pos, radius))
    }
}

object GraffelRepository {
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
    }

  def transactional[M[_]: Monad](xa: Transactor[M]): GraffelRepository[M] =
    connectionIo.mapK(xa.trans)
}

object GraffelStatements {
  def query(id: Id[Graffel]): Query0[Entity[Graffel]] =
    sql"""
      SELECT id, latitude, longitude FROM graffel WHERE id = ${id.value}
    """.query

  def insert(graffel: Graffel): Update0 =
    sql"""
      INSERT INTO graffel (latitude, longitude)
      VALUES (
        ${graffel.position.latitude},
        ${graffel.position.longitude}
      )
    """.update

  def insert(tag: Tag): Update0 =
    sql"""
         INSERT INTO tag (name, graffel_id)
         VALUES (
          ${tag.name},
          ${tag.graffelId}
         )
       """.update

  def findTagsByPosition(pos: Position, radius: Radius): Query0[Entity[Tag]] = {
    val distanceUnit = radius.unit
    val factor = distanceUnit match {
      case _: meter.type     => 1000
      case _: kilometer.type => 1
    }
    val radFactor = Math.PI / 180
    val lonRad = pos.longitude.value * radFactor
    val latRad = pos.latitude.value * radFactor
    val earth = 6371.0088 * factor
    sql"""select t.id, t.name, t.graffel_id from
               (select id,
                      ( $earth * acos( cos( $latRad )
                             * cos( radians( graffel.latitude ) )
                             * cos( radians( graffel.longitude ) - $lonRad )
                             + sin( $latRad )
                             * sin( radians( graffel.latitude ) ) ) ) AS distance
               from graffel) as g
               join tag t on (t.graffel_id = g.id)
               where g.distance < ${radius.length}
               order by g.distance asc
             """.query
  }
}
