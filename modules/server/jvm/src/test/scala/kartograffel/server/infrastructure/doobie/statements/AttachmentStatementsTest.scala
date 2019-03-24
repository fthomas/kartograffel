package kartograffel.server.infrastructure.doobie.statements

import kartograffel.server.domain.model.Graffel
import kartograffel.server.infrastructure.doobie.DbSpecification
import kartograffel.server.ArbitraryInstances._
import kartograffel.shared.model.Id

class AttachmentStatementsTest extends DbSpecification {
  "create" >>
    check(AttachmentStatements.create(sampleOf[Id[Graffel]], sampleOf[Id[File]]))

  "findByGraffel" >>
    check(AttachmentStatements.findFilesByGraffel(sampleOf[Id[Graffel]]))
}
