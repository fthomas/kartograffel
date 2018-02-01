package kartograffel.server.infrastructure.doobie.statements

import kartograffel.shared.domain.model.{User, Username}
import kartograffel.shared.model.ArbitraryInstances._
import kartograffel.shared.model.Id

class UserStatementsTest
    extends EntityStatementsTest(UserStatements, sampleOf[User], sampleOf[Id[User]]) {

  "findByName" >>
    check(UserStatements.findByName(sampleOf[Username]))
}
