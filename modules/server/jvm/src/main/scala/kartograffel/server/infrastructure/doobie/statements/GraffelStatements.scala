package kartograffel.server.infrastructure.doobie.statements

import doobie._
import doobie.implicits._
import kartograffel.shared.domain.model.Tag
import kartograffel.shared.model._

object GraffelStatements extends EntityStatements[Graffel] {
  override def create(graffel: Graffel): Update0 =
    sql"""
      INSERT INTO graffel (latitude, longitude)
      VALUES (
        ${graffel.position.latitude},
        ${graffel.position.longitude}
      )
    """.update

  override def findById(id: Id[Graffel]): Query0[Entity[Graffel]] =
    sql"""
      SELECT id, latitude, longitude FROM graffel WHERE id = $id
    """.query

  override def deleteAll: Update0 =
    sql"DELETE FROM graffel".update

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

  def findGraffelByPosition(pos: Position): Query0[Entity[Graffel]] =
    sql"""select * from graffel where latitude = ${pos.latitude} and longitude = ${pos.longitude}""".query

  def findTagsByGraffel(id: Id[Graffel]): Query0[Entity[Tag]] =
    sql"""select * from tag where graffel_id = ${id.value}""".query
}
