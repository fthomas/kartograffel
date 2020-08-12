package kartograffel.server.infrastructure.doobie.statements
import kartograffel.server.infrastructure.doobie.DbSpecification._
import kartograffel.server.ArbitraryInstances._
import kartograffel.server.domain.model.{GraffelId, Radius, Tag}
import kartograffel.shared.domain.model.Position
import org.scalatest.funsuite.AnyFunSuite

class TagStatementsTest extends AnyFunSuite {
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
