package kartograffel.server.infrastructure.doobie.migration

import doobie._
import doobie.implicits._
import kartograffel.server.infrastructure.doobie.DoobieMigration

class V0004__CreateFile extends DoobieMigration {
  override def migrate: ConnectionIO[_] =
    sql"""
      CREATE TABLE file (
        id          BIGSERIAL    PRIMARY KEY,
        mime_type   VARCHAR(255) NOT NULL,
        uploaded_at TIMESTAMP    NOT NULL,
        content     BYTEA        NOT NULL
      )
    """.update.run
}
