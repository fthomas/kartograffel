package kartograffel.server.db

import fs2.interop.cats._
import kartograffel.shared.model.ArbitraryInstances._
import kartograffel.shared.model.Graffel

class GraffelRepositorySpec extends DbSpecification {
  private val gr = GraffelRepository.transactional(transactor)

  "GraffelRepository" >> {
    "insert and query" >> {
      val graffel = sampleOf[Graffel]
      val inserted = gr.insert(graffel).unsafeRun()
      val queried = gr.query(inserted.id).unsafeRun()
      Option(inserted) must_=== queried
    }
  }
}
