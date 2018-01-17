package kartograffel.server.infrastructure.doobie.statements

import doobie._
import doobie.implicits._
import kartograffel.server.infrastructure.doobie.DoobieInstances
import kartograffel.shared.domain.model.{User, Username}
import kartograffel.shared.model.{Entity, Id}

object UserStatements extends DoobieInstances {
  def create(user: User): Update0 =
    sql"INSERT INTO user (name) VALUES (${user.name})".update

  def deleteAll: Update0 =
    sql"DELETE FROM user".update

  def findById(id: Id[User]): Query0[Entity[User]] =
    sql"SELECT * FROM user WHERE id = $id".query

  def findByName(name: Username): Query0[Entity[User]] =
    sql"SELECT * FROM user WHERE name = $name".query
}
