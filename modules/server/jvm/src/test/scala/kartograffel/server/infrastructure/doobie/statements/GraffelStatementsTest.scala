package kartograffel.server.infrastructure.doobie.statements

import kartograffel.server.domain.model.{Graffel, Position, Tag}
import kartograffel.shared.domain.model.{Position, Tag}
import kartograffel.server.ArbitraryInstances._
import kartograffel.shared.model._

class GraffelStatementsTest extends EntityStatementsTest(GraffelStatements) {
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

  "insert" >>
    check(GraffelStatements.insert(sampleOf[Tag]))
}
