package kartograffel.server.infrastructure.doobie.statements

import doobie._
import doobie.implicits._
import kartograffel.server.domain.model.{Graffel, GraffelId}
import kartograffel.shared.domain.model.{GraffelId, Tag}
import kartograffel.shared.model._

object GraffelStatements {
  def create(graffel: Graffel): Update0 =
    sql"""
      INSERT INTO graffel (id, latitude, longitude)
      VALUES (${graffel.id}, ${graffel.position.latitude}, ${graffel.position.longitude})
    """.update

  def findById(id: GraffelId): Query0[Graffel] =
    sql"SELECT id, latitude, longitude FROM graffel WHERE id = $id".query
}
