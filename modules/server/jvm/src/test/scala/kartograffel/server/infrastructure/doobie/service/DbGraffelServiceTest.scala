package kartograffel.server.infrastructure.doobie.service
import eu.timepit.refined.refineMV
import org.scalatest.{FunSuite, Matchers}
import kartograffel.server.ArbitraryInstances._
import kartograffel.server.domain.model.Radius.LengthRange
import kartograffel.server.domain.model.{kilometer, Radius}
import kartograffel.server.infrastructure.doobie.DbSpecification._
import kartograffel.shared.domain.model.Position

class DbGraffelServiceTest extends FunSuite with Matchers {

  test("create") {
    val name = sampleOf[String]
    val pos = sampleOf[Position]
    val q = for {
      _ <- DbGraffelService.create(name, pos)
      _ <- DbGraffelService.create(name, pos)
      g <- DbGraffelService.findNearPosition(pos, Radius(refineMV[LengthRange](1), kilometer))
    } yield g

    val result = runQuery(q).unsafeRunSync()

    result.size should be(1)
    result.head._1.name should be(name)
    result.head._2.position should be(pos)
  }

}
