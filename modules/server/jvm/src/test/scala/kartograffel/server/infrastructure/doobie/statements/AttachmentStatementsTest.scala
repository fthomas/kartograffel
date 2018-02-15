package kartograffel.server.infrastructure.doobie.statements

import kartograffel.server.infrastructure.doobie.DbSpecification
import kartograffel.shared.domain.model.File
import kartograffel.shared.model.ArbitraryInstances._
import kartograffel.shared.model.{Graffel, Id}

class AttachmentStatementsTest extends DbSpecification {
  "create" >>
    check(AttachmentStatements.create(sampleOf[Id[Graffel]], sampleOf[Id[File]]))

  "findByGraffel" >>
    check(AttachmentStatements.findFilesByGraffel(sampleOf[Id[Graffel]]))
}
