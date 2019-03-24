package kartograffel.server.infrastructure.doobie.repository

import kartograffel.shared.domain.model.User
import kartograffel.server.ArbitraryInstances._

class DbUserRepositoryTest extends DbEntityRepositoryTest(DbUserRepository) {
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
