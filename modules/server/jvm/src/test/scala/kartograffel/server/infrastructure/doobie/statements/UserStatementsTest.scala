package kartograffel.server.infrastructure.doobie.statements

import kartograffel.server.infrastructure.doobie.DbSpecification
import kartograffel.shared.domain.model.{User, Username}
import kartograffel.shared.model.ArbitraryInstances._
import kartograffel.shared.model.Id

class UserStatementsTest extends DbSpecification {
  "create" >>
    check(UserStatements.create(sampleOf[User]))

  "deleteAll" >>
    check(UserStatements.deleteAll)

  "findById" >>
    check(UserStatements.findById(sampleOf[Id[User]]))

  "findByName" >>
    check(UserStatements.findByName(sampleOf[Username]))
}
