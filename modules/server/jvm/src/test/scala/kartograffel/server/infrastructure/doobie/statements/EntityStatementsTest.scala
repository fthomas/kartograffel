package kartograffel.server.infrastructure.doobie.statements

import kartograffel.server.infrastructure.doobie.DbSpecification
import kartograffel.shared.model.Id

import scala.reflect.runtime.universe.TypeTag

abstract class EntityStatementsTest[T: TypeTag](
    statements: EntityStatements[T],
    sampleEntity: T,
    sampleId: Id[T]
) extends DbSpecification {

  "create" >>
    check(statements.create(sampleEntity))

  "deleteAll" >>
    check(statements.deleteAll)

  "findById" >>
    check(statements.findById(sampleId))
}
