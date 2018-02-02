package kartograffel.server.infrastructure.doobie

import cats.effect.IO
import cats.implicits._
import doobie.implicits._
import doobie.specs2.analysisspec.IOChecker
import doobie.{ConnectionIO, Transactor}
import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyString
import kartograffel.server.BuildInfo
import kartograffel.server.application.Config
import org.specs2.mutable.Specification

trait DbSpecification extends Specification with IOChecker {
  DbSpecification.runMigrationOnce

  override def transactor: Transactor[IO] =
    DbSpecification.transactor

  implicit class ConnectionIoOps[A](val self: ConnectionIO[A]) {
    def yolo: A = self.transact(transactor).unsafeRunSync()
  }
}

object DbSpecification {
  val dbConfig: Config.Db = {
    val path = s"${BuildInfo.crossTarget.toString}/h2/kartograffel"
    val url = NonEmptyString.unsafeFrom(s"jdbc:h2:$path;MODE=PostgreSQL;AUTO_SERVER=TRUE")

    Config.Db(
      driver = "org.h2.Driver",
      url = url,
      user = "",
      password = ""
    )
  }

  lazy val runMigrationOnce: Unit =
    DoobieMigration.run[IO](dbConfig).void.unsafeRunSync()

  val transactor: Transactor[IO] =
    DoobieUtils.transactor[IO](dbConfig).unsafeRunSync()
}
