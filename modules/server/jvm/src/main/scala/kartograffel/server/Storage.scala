package kartograffel.server

import doobie.imports._
import fs2.Task
import kartograffel.shared.model.Graffel

object Storage {
  val transactor: Transactor[Task] = {
    val settings = Map("MODE" -> "PostgreSQL", "AUTO_SERVER" -> "TRUE")
      .map { case (key, value) => s"$key=$value" }
      .mkString(";", ";", "")

    DriverManagerTransactor(
      driver = "org.h2.Driver",
      url = s"jdbc:h2:~/.kartograffel/db/${BuildInfo.name}$settings"
    )
  }

  val create: Update0 = sql"""
    DROP TABLE IF EXISTS graffel;
    CREATE TABLE graffel (
      id BIGSERIAL PRIMARY KEY,
      latitude  DOUBLE NOT NULL,
      longitude DOUBLE NOT NULL
    )
  """.update

  def create2 =
    create.run.transact(transactor).unsafeRun()

  def insertGraffel(graffel: Graffel): Update0 = {
    println(graffel)
    sql"""
        INSERT INTO graffel (id, latitude, longitude) VALUES (DEFAULT, ${graffel.position.latitude.value},
        ${graffel.position.longitude.value})
    """.update
  }

  val now: Task[String] = {
    val query: Query0[String] = sql"select now()".query
    query.unique.transact(transactor)
  }
}
