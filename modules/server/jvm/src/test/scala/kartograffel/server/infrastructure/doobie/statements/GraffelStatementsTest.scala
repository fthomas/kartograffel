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

  "insert(Graffel)" >>
    check(GraffelStatements.insert(sampleOf[Graffel]))

  "insert(Tag)" >>
    check(GraffelStatements.insert(sampleOf[Tag]))

  "query" >>
    check(GraffelStatements.query(sampleOf[Id[Graffel]]))
}
