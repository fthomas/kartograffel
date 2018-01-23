package kartograffel.server.infrastructure.doobie.repository

import kartograffel.shared.domain.model.User
import kartograffel.shared.model.ArbitraryInstances
import kartograffel.shared.model.ArbitraryInstances.sampleOf
import org.scalacheck.Arbitrary

class DbUserRepositoryTest extends DbEntityRepositoryTest[User] {
  override implicit def arbitraryT: Arbitrary[User] =
    ArbitraryInstances.userArbitrary

  override def repository: DbEntityRepository[User] =
    DbUserRepository

  "findByName" >> {
    val user = sampleOf[User]
    val prg = for {
      _ <- DbUserRepository.deleteAll
      _ <- DbUserRepository.create(user)
      user1 <- DbUserRepository.findByName(user.name)
    } yield user1
    prg.yolo.map(_.value) should_=== Some(user)
  }
}
