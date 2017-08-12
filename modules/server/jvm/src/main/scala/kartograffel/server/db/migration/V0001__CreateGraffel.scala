package kartograffel.server.db.migration

import doobie.imports._
import doobie.util.transactor.Transactor
import fs2.Task
import fs2.interop.cats._
import kartograffel.server.db.DoobieMigration

class V0001__CreateGraffel extends DoobieMigration {
  override def migrate(xa: Transactor[Task]): Task[_] = {
    val create: Update0 = sql"""
      CREATE TABLE graffel (
        id BIGSERIAL PRIMARY KEY,
        latitude  DOUBLE NOT NULL,
        longitude DOUBLE NOT NULL
      )
    """.update
    create.run.transact(xa)
  }
}
