package kartograffel.server.infrastructure.doobie.statements

import kartograffel.server.infrastructure.doobie.DbSpecification
import kartograffel.server.ArbitraryInstances._
import kartograffel.shared.model.Id
import org.scalacheck.Arbitrary

import scala.reflect.runtime.universe.TypeTag

abstract class EntityStatementsTest[T: Arbitrary: TypeTag](statements: EntityStatements[T])
    extends DbSpecification {

  "create" >>
    check(statements.create(sampleOf[T]))

  "deleteAll" >>
    check(statements.deleteAll)

  "findById" >>
    check(statements.findById(sampleOf[Id[T]]))
}
