package kartograffel.server.infrastructure.doobie.repository

import eu.timepit.refined.auto._
import kartograffel.server.db.DbSpecification
import kartograffel.shared.domain.model.{User, Username}

class DbUserRepositoryTest extends DbSpecification {
  "create" >> {
    val alice = User(Username("alice"))
    val prg = for {
      _ <- DbUserRepository.deleteAll
      user <- DbUserRepository.create(alice)
    } yield user
    prg.yolo.value should_=== alice
  }

  "findById" >> {
    val fred = User(Username("fred"))
    val prg = for {
      _ <- DbUserRepository.deleteAll
      entity <- DbUserRepository.create(fred)
      user <- DbUserRepository.findById(entity.id)
    } yield user
    prg.yolo.map(_.value) should_=== Some(fred)
  }

  "findByName" >> {
    val bob = User(Username("bob"))
    val prg = for {
      _ <- DbUserRepository.deleteAll
      _ <- DbUserRepository.create(bob)
      user <- DbUserRepository.findByName(bob.name)
    } yield user
    prg.yolo.map(_.value) should_=== Some(bob)
  }
}
