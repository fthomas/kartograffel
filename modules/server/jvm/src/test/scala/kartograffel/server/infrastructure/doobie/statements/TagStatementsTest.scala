package kartograffel.server.infrastructure.doobie.statements
import org.scalatest.FunSuite
import kartograffel.server.infrastructure.doobie.DbSpecification._
import kartograffel.server.ArbitraryInstances._
import kartograffel.server.domain.model.{GraffelId, Radius, Tag}
import kartograffel.shared.domain.model.Position

class TagStatementsTest extends FunSuite {
  test("find") {
    check(TagStatements.find(sampleOf[String], sampleOf[GraffelId]))
  }

  test("findTagsByPosition") {
    check(TagStatements.findTagsByPosition(sampleOf[Position], sampleOf[Radius]))
  }

  test("create") {
    check(TagStatements.create(sampleOf[Tag]))
  }

}
