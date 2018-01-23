package kartograffel.server.infrastructure.doobie.statements

import kartograffel.server.db.DbSpecification
import kartograffel.shared.model.ArbitraryInstances._
import kartograffel.shared.model._

class GraffelStatementsTest extends DbSpecification {
  "findGraffelByPosition" >>
    check(GraffelStatements.findGraffelByPosition(sampleOf[Position]))

  "findTagsByGraffel" >>
    check(GraffelStatements.findTagsByGraffel(sampleOf[Id[Graffel]]))

  /*
  "findTagsByPosition" >>
    check(
      GraffelStatements.findTagsByPosition(sampleOf[Position],
                                           sampleOf[Radius]))
   */

  "create" >>
    check(GraffelStatements.create(sampleOf[Graffel]))

  "insert" >>
    check(GraffelStatements.insert(sampleOf[Tag]))

  "findById" >>
    check(GraffelStatements.findById(sampleOf[Id[Graffel]]))
}
