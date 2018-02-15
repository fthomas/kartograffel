package kartograffel.server.infrastructure.doobie.repository

import cats.implicits._
import doobie._
import doobie.implicits._
import fs2.Stream
import kartograffel.server.domain.repository.FileRepository
import kartograffel.server.infrastructure.doobie.statements.{AttachmentStatements, FileStatements}
import kartograffel.shared.domain.model.File
import kartograffel.shared.model.{Entity, Graffel, Id}

object DbFileRepository extends FileRepository[ConnectionIO] with DbEntityRepository[File] {
  override def statements: FileStatements.type =
    FileStatements

  override def attach(file: File, graffelId: Id[Graffel]): ConnectionIO[Entity[File]] =
    create(file).flatTap(entity => AttachmentStatements.create(graffelId, entity.id).run)

  override def findByGraffel(id: Id[Graffel]): Stream[ConnectionIO, Entity[File]] =
    AttachmentStatements.findFilesByGraffel(id).stream
}
