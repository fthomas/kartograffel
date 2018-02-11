package kartograffel.server.infrastructure.doobie.statements

import doobie._
import doobie.implicits._
import kartograffel.shared.domain.model.File
import kartograffel.shared.model.{Entity, Id}

object FileStatements extends EntityStatements[File] {
  override def create(file: File): Update0 =
    sql"""
      INSERT INTO file (graffel_id, mime_type, created_at, content)
      VALUES (
        ${file.graffel},
        ${file.mimeType},
        ${file.createdAt},
        ${file.content}
      )
    """.update

  override def deleteAll: Update0 =
    sql"DELETE FROM file".update

  override def findById(id: Id[File]): Query0[Entity[File]] =
    sql"SELECT * FROM file WHERE id = $id".query
}
