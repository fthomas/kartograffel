package kartograffel.server.domain.repository

import fs2.Stream
import kartograffel.shared.domain.model.File
import kartograffel.shared.model.{Entity, Graffel, Id}

trait FileRepository[F[_]] extends EntityRepository[F, File] {
  def attach(file: File, graffelId: Id[Graffel]): F[Entity[File]]

  def findByGraffel(id: Id[Graffel]): Stream[F, Entity[File]]
}
