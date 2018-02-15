package kartograffel.server.infrastructure.doobie.migration

import doobie._
import doobie.implicits._
import kartograffel.server.infrastructure.doobie.DoobieMigration

final class V0003__CreateUser extends DoobieMigration {
  override def migrate: ConnectionIO[_] =
    sql"""
      CREATE TABLE user (
        id         BIGSERIAL    PRIMARY KEY,
        name       VARCHAR(255) NOT NULL,
        created_at TIMESTAMP    NOT NULL
      )
    """.update.run
}
