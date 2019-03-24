package kartograffel.server.infrastructure.doobie.statements

import kartograffel.shared.domain.model.Username
import kartograffel.server.ArbitraryInstances._

class UserStatementsTest extends EntityStatementsTest(UserStatements) {
  "findByName" >>
    check(UserStatements.findByName(sampleOf[Username]))
}
