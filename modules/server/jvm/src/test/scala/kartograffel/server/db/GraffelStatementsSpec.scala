package kartograffel.server.db

import kartograffel.shared.model.ArbitraryInstances._
import kartograffel.shared.model.{Graffel, Id}

class GraffelStatementsSpec extends DbSpecification {
  check(GraffelStatements.query(sampleOf[Id[Graffel]]))
  check(GraffelStatements.insert(sampleOf[Graffel]))
}
