package kartograffel.server

import doobie.imports._
import fs2.Task

object Storage {
  val transactor: Transactor[Task] = {
    val settings = Map("MODE" -> "PostgreSQL")
      .map { case (key, value) => s"$key=$value" }
      .mkString(";", ";", "")

    DriverManagerTransactor(
      driver = "org.h2.Driver",
      url = s"jdbc:h2:./db/${BuildInfo.name}$settings"
    )
  }

  val now: Task[String] = {
    val query: Query0[String] = sql"select now()".query
    query.unique.transact(transactor)
  }
}
