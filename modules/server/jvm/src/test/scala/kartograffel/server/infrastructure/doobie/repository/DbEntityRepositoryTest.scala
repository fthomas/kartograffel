package kartograffel.server.infrastructure.doobie.repository

import kartograffel.server.infrastructure.doobie.DbSpecification
import kartograffel.shared.model.ArbitraryInstances.sampleOf
import org.scalacheck.Arbitrary

abstract class DbEntityRepositoryTest[T: Arbitrary](repository: DbEntityRepository[T])
    extends DbSpecification {

  "create" >> {
    val value = sampleOf[T]
    val prg = for {
      _ <- repository.deleteAll
      value1 <- repository.create(value)
    } yield value1
    prg.yolo.value should_=== value
  }

  "findById" >> {
    val value = sampleOf[T]
    val prg = for {
      _ <- repository.deleteAll
      entity <- repository.create(value)
      value1 <- repository.findById(entity.id)
    } yield value1
    prg.yolo.map(_.value) should_=== Some(value)
  }
}
