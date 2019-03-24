package kartograffel.server.infrastructure.doobie.statements
import doobie.{Query0, Update0}
import kartograffel.shared.domain.model._
import kartograffel.shared.model.{kilometer, meter, Position}
import doobie.implicits._
import kartograffel.server.domain.model._

object TagStatements {

  def insert(tag: Tag): Update0 =
    sql"""
         INSERT INTO tag (name, graffel_id)
         VALUES (
          ${tag.name},
          ${tag.graffelId}
         )
       """.update

  def findTagsByPosition(pos: Position, radius: Radius): Query0[Tag] = {
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
