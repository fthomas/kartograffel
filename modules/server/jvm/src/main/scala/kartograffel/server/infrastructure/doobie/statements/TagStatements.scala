package kartograffel.server.infrastructure.doobie.statements
import doobie.implicits._
import doobie.{Query0, Update0}
import kartograffel.server.domain.model._
import kartograffel.server.infrastructure.doobie.DoobieInstances
import kartograffel.shared.domain.model.Position

object TagStatements extends DoobieInstances {
  def create(tag: Tag): Update0 =
    sql"""
         INSERT INTO tag (id, name, graffel_id)
         VALUES (${tag.id}, ${tag.name}, ${tag.graffelId})
       """.update

  def find(name: String, graffelId: GraffelId): Query0[Tag] =
    sql"SELECT ID, NAME, GRAFFEL_ID FROM TAG WHERE NAME=$name AND GRAFFEL_ID=$graffelId".query

  def findTagsByPosition(pos: Position, radius: Radius): Query0[(Tag, Graffel)] = {
    val distanceUnit = radius.unit
    val factor = distanceUnit match {
      case _: meter.type     => 1000
      case _: kilometer.type => 1
    }
    val radFactor = Math.PI / 180
    val lonRad = pos.longitude.value * radFactor
    val latRad = pos.latitude.value * radFactor
    val earth = 6371.0088 * factor
    sql"""select t.id, t.name, t.graffel_id, g.id, g.latitude, g.longitude from
               (select id,
                      ( $earth * acos( cos( $latRad )
                             * cos( radians( graffel.latitude ) )
                             * cos( radians( graffel.longitude ) - $lonRad )
                             + sin( $latRad )
                             * sin( radians( graffel.latitude ) ) ) ) AS distance,
                             graffel.latitude,
                             graffel.longitude
               from graffel) as g
               join tag t on (t.graffel_id = g.id)
               where g.distance < ${radius.length}
               order by g.distance asc
             """.query
  }
}
