package kartograffel.server.infrastructure.doobie.repository
import kartograffel.server.ArbitraryInstances._
import kartograffel.server.domain.model.Graffel
import kartograffel.server.infrastructure.doobie.DbSpecification._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class DbGraffelRepositoryTest extends AnyFunSuite with Matchers {
  test("findByPosition") {
    val g = sampleOf[Graffel]

    val q = for {
      _ <- DbGraffelRepository.create(g)
      s <- DbGraffelRepository.findByPosition(g.position)
    } yield s

    val result = runQuery(q).unsafeRunSync()

    result should be(defined)
    result.map(_.id) should contain(g.id)
    result.map(_.position) should contain(g.position)
  }

  test("findById") {
    val g = sampleOf[Graffel]

    val q = for {
      _ <- DbGraffelRepository.create(g)
      s <- DbGraffelRepository.findById(g.id)
    } yield s

    val result = runQuery(q).unsafeRunSync()

    result should be(defined)
    result.map(_.id) should contain(g.id)
    result.map(_.position) should contain(g.position)
  }

  test("create") {
    val g = sampleOf[Graffel]

    val q = for {
      _ <- DbGraffelRepository.create(g)
      s <- DbGraffelRepository.findById(g.id)
    } yield s

    val result = runQuery(q).unsafeRunSync()

    result should be(defined)
    result.map(_.id) should contain(g.id)
    result.map(_.position) should contain(g.position)
  }
}
