package kartograffel.server

import doobie.imports._
import eu.timepit.refined.auto._
import fs2.Task
import kartograffel.shared.model.Graffel

object Storage {
  def transactorFrom(db: Config.Db): Transactor[Task] =
    DriverManagerTransactor(driver = db.driver, url = db.url)

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

  def insertGraffel(graffel: Graffel): Update0 = {
    println(graffel)
    sql"""
        INSERT INTO graffel (id, latitude, longitude) VALUES (DEFAULT, ${graffel.position.latitude.value},
        ${graffel.position.longitude.value})
    """.update
  }
}
