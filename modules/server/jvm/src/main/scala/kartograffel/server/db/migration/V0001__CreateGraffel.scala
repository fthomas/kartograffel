package kartograffel.server.db.migration

import doobie._
import doobie.implicits._
import kartograffel.server.db.DoobieMigration

final class V0001__CreateGraffel extends DoobieMigration {
  override def migrate: ConnectionIO[_] =
    sql"""
      CREATE TABLE graffel (
        id BIGSERIAL PRIMARY KEY,
        latitude  DOUBLE NOT NULL,
        longitude DOUBLE NOT NULL
      )
    """.update.run
}
