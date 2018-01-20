package kartograffel.server.infrastructure.doobie.repository

import kartograffel.server.db.DbSpecification
import kartograffel.shared.domain.model.User
import kartograffel.shared.model.ArbitraryInstances._

class DbUserRepositoryTest extends DbSpecification {
  "create" >> {
    val user = sampleOf[User]
    val prg = for {
      _ <- DbUserRepository.deleteAll
      user1 <- DbUserRepository.create(user)
    } yield user1
    prg.yolo.value should_=== user
  }

  "findById" >> {
    val user = sampleOf[User]
    val prg = for {
      _ <- DbUserRepository.deleteAll
      entity <- DbUserRepository.create(user)
      user1 <- DbUserRepository.findById(entity.id)
    } yield user1
    prg.yolo.map(_.value) should_=== Some(user)
  }

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
