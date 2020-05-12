package kartograffel.server.infrastructure.doobie
import java.util.UUID

import cats.effect.{Async, ContextShift, IO, Sync}
import cats.implicits._
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.specs2._
import doobie.util.transactor.Transactor
import eu.timepit.refined.types.string.NonEmptyString
import kartograffel.server.application.Config
import org.specs2.mutable.Specification

import scala.concurrent.ExecutionContext

object DbSpecification extends Specification with IOChecker {
  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  override def transactor: doobie.Transactor[IO] =
    Transactor.fromDriverManager[IO](
      dbConfig.driver.value,
      dbConfig.url.value,
      dbConfig.user,
      dbConfig.password
    )

  lazy val dbConfig =
    Config.Db(
      driver = NonEmptyString("org.h2.Driver"),
      url = NonEmptyString("jdbc:h2:mem:;MODE=PostgreSQL"),
      user = "",
      password = ""
    )

  def runQuery[F[_]: ContextShift: Async, G](connectionIO: ConnectionIO[G]): F[G] =
    for {
      d <- Sync[F].delay {
        val id = UUID.randomUUID()
        //inMem with more than one operation needs a name so the instance can be found
        dbConfig.copy(
          url = NonEmptyString.unsafeFrom(s"jdbc:h2:mem:${id.toString};MODE=PostgreSQL")
        )
      }
      c <-
        DoobieUtils
          .transactor[F](d)
          .use(tx => (DoobieMigration.run[ConnectionIO](d) >> connectionIO).transact(tx))
    } yield c
}
