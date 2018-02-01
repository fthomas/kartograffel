package kartograffel.server.infrastructure.doobie.repository

import cats.data.OptionT
import doobie._
import doobie.implicits._
import kartograffel.server.domain.repository.GraffelRepository
import kartograffel.server.infrastructure.doobie.statements.GraffelStatements
import kartograffel.shared.model._

object DbGraffelRepository
    extends GraffelRepository[ConnectionIO]
    with DbEntityRepository[Graffel] {

  override def statements: GraffelStatements.type =
    GraffelStatements

  override def insert(tag: Tag): ConnectionIO[Entity[Tag]] =
    GraffelStatements
      .insert(tag)
      .withUniqueGeneratedKeys[Id[Tag]]("id")
      .map(Entity(_, tag))

  override def findTagsByPosition(
      pos: Position,
      radius: Radius): ConnectionIO[List[Entity[Tag]]] =
    GraffelStatements.findTagsByPosition(pos, radius).list

  override def findGraffelByPosition(
      position: Position): ConnectionIO[Option[Entity[Graffel]]] =
    GraffelStatements.findGraffelByPosition(position).option

  override def findByPositionOrCreate(
      position: Position): ConnectionIO[Entity[Graffel]] =
    OptionT(findGraffelByPosition(position))
      .getOrElseF(create(Graffel(position)))

  override def findTagsByGraffel(
      id: Id[Graffel]): ConnectionIO[List[Entity[Tag]]] =
    GraffelStatements.findTagsByGraffel(id).list
}
