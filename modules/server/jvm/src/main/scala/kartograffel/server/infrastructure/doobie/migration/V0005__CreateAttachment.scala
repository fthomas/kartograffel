package kartograffel.server.infrastructure.doobie.migration

import doobie._
import doobie.implicits._
import kartograffel.server.infrastructure.doobie.DoobieMigration

class V0005__CreateAttachment extends DoobieMigration {
  override def migrate: ConnectionIO[_] =
    sql"""
      CREATE TABLE attachment (
        graffel_id BIGSERIAL REFERENCES graffel (id),
        file_id    BIGSERIAL REFERENCES file (id)
      )
    """.update.run
}
