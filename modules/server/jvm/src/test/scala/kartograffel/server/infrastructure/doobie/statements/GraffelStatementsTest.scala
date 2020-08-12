package kartograffel.server.infrastructure.doobie.statements
import kartograffel.server.infrastructure.doobie.DbSpecification._
import kartograffel.server.ArbitraryInstances._
import kartograffel.server.domain.model.{Graffel, GraffelId}
import kartograffel.shared.domain.model.Position
import org.scalatest.funsuite.AnyFunSuite

class GraffelStatementsTest extends AnyFunSuite {
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
