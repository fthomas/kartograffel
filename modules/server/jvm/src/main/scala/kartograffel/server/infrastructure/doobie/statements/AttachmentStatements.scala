package kartograffel.server.infrastructure.doobie.statements

import doobie._
import doobie.implicits._
import kartograffel.server.infrastructure.doobie.DoobieInstances
import kartograffel.shared.domain.model.File
import kartograffel.shared.model.{Entity, Graffel, Id}

object AttachmentStatements extends DoobieInstances {
  def create(graffelId: Id[Graffel], fileId: Id[File]): Update0 =
    sql"""
      INSERT INTO attachment (graffel_id, file_id)
      VALUES ($graffelId, $fileId)
    """.update

  def findFilesByGraffel(id: Id[Graffel]): Query0[Entity[File]] =
    sql"""
      SELECT id, mime_type, uploaded_at, content FROM file
      INNER JOIN attachment ON attachment.file_id = file.id
      WHERE attachment.graffel_id = $id
    """.query
}
