package kartograffel.server.db

import kartograffel.server.domain.repository.GraffelRepository
import kartograffel.shared.model.ArbitraryInstances._
import kartograffel.shared.model.Graffel

class GraffelRepositorySpec extends DbSpecification {
  private val gr = GraffelRepository.transactional(transactor)

  "GraffelRepository" >> {
    "insert and query" >> {
      val graffel = sampleOf[Graffel]
      val inserted = gr.create(graffel).unsafeRunSync()
      val queried = gr.findById(inserted.id).unsafeRunSync()
      Option(inserted) must_=== queried
    }
  }
}
