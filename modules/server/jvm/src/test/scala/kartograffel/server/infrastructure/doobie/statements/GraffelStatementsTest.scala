package kartograffel.server.infrastructure.doobie.statements
import org.scalatest.FunSuite
import kartograffel.server.infrastructure.doobie.DbSpecification._
import kartograffel.server.ArbitraryInstances._
import kartograffel.server.domain.model.{Graffel, GraffelId}
import kartograffel.shared.domain.model.Position

class GraffelStatementsTest extends FunSuite {
  test("create") {
    check(GraffelStatements.create(sampleOf[Graffel]))
  }

  test("findById") {
    check(GraffelStatements.findById(sampleOf[GraffelId]))
  }

  test("findByPosition") {
    check(GraffelStatements.findByPosition(sampleOf[Position]))
  }
}
