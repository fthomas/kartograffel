package kartograffel.server.infrastructure.doobie.statements

import doobie._
import doobie.implicits._
import kartograffel.server.domain.model.{Graffel, GraffelId}
import kartograffel.server.infrastructure.doobie.DoobieInstances
import kartograffel.shared.domain.model.Position

object GraffelStatements extends DoobieInstances {
  def create(graffel: Graffel): Update0 =
    sql"""
      INSERT INTO graffel (id, latitude, longitude)
      VALUES (${graffel.id}, ${graffel.position.latitude}, ${graffel.position.longitude})
    """.update

  def findById(id: GraffelId): Query0[Graffel] =
    sql"SELECT id, latitude, longitude FROM graffel WHERE id = $id".query

  def findByPosition(p: Position): Query0[Graffel] =
    sql"SELECT ID, LATITUDE, LONGITUDE FROM GRAFFEL WHERE LATITUDE=${p.latitude} AND LONGITUDE=${p.longitude}".query
}
