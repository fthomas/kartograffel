package kartograffel.server.infrastructure.doobie.statements

import kartograffel.shared.domain.model.Username
import kartograffel.shared.model.ArbitraryInstances._

class UserStatementsTest extends EntityStatementsTest(UserStatements) {
  "findByName" >>
    check(UserStatements.findByName(sampleOf[Username]))
}
