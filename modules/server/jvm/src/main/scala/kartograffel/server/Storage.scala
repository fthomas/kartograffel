package kartograffel.server

import fs2.interop.cats._
import doobie.imports._
import eu.timepit.refined.auto._
import fs2.Task

object Storage {
  def transactorFrom(db: Config.Db): Transactor[Task] =
    DriverManagerTransactor[Task](driver = db.driver, url = db.url)

  val create: Update0 = sql"""
    DROP TABLE IF EXISTS graffel;
    CREATE TABLE graffel (
      id BIGSERIAL PRIMARY KEY,
      latitude  DOUBLE NOT NULL,
      longitude DOUBLE NOT NULL
    )
  """.update

  def create2(transactor: Transactor[Task]) =
    create.run.transact(transactor).unsafeRun()

}
