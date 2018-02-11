package kartograffel.server.infrastructure.doobie.repository

import doobie._
import kartograffel.server.domain.repository.FileRepository
import kartograffel.server.infrastructure.doobie.statements.FileStatements
import kartograffel.shared.domain.model.File

object DbFileRepository extends FileRepository[ConnectionIO] with DbEntityRepository[File] {
  override def statements: FileStatements.type =
    FileStatements
}
