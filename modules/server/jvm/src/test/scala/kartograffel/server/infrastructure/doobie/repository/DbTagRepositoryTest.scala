package kartograffel.server.infrastructure.doobie.repository
import eu.timepit.refined._
import kartograffel.server.ArbitraryInstances._
import kartograffel.server.domain.model.Radius.LengthRange
import kartograffel.server.domain.model._
import kartograffel.server.infrastructure.doobie.DbSpecification._
import org.scalatest.{FunSuite, Matchers}

class DbTagRepositoryTest extends FunSuite with Matchers {

  test("create") {
    val graffel = sampleOf[Graffel]
    val tag = sampleOf[Tag].copy(graffelId = graffel.id)
    val q = for {
      _ <- DbGraffelRepository.create(graffel)
      _ <- DbTagRepository.create(tag)
      t <- DbTagRepository.find(tag.name, tag.graffelId)
    } yield t

    val result = runQuery(q).unsafeRunSync()

    result should be(defined)
    result.get should be(tag)
  }

  test("find"){
    val graffel = sampleOf[Graffel]
    val tag = sampleOf[Tag].copy(graffelId = graffel.id)
    val q = for {
      _ <- DbGraffelRepository.create(graffel)
      _ <- DbTagRepository.create(tag)
      t <- DbTagRepository.find(tag.name, tag.graffelId)
    } yield t

    val result = runQuery(q).unsafeRunSync()

    result should be(defined)
    result.get should be(tag)
  }

  test("findTagsByPosition"){
    val graffel = sampleOf[Graffel]
    val tag = sampleOf[Tag].copy(graffelId = graffel.id)
    val q = for {
      _ <- DbGraffelRepository.create(graffel)
      _ <- DbTagRepository.create(tag)
      t <- DbTagRepository.findTagsByPosition(graffel.position, Radius(refineMV[LengthRange](1), kilometer))
    } yield t

    val result = runQuery(q).unsafeRunSync()

    result should not be empty
    result.head should be((tag, graffel))
  }

}
