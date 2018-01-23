package kartograffel.server.infrastructure.doobie.repository

import kartograffel.shared.model.{ArbitraryInstances, Graffel}
import org.scalacheck.Arbitrary

class DbGraffelRepositoryTest extends DbEntityRepositoryTest[Graffel] {
  override implicit def arbitraryT: Arbitrary[Graffel] =
    ArbitraryInstances.graffelArbitrary

  override def repository: DbEntityRepository[Graffel] =
    DbGraffelRepository
}
