package kartograffel.server.infrastructure.doobie.repository

import cats.implicits._
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
      position: Position): ConnectionIO[Entity[Graffel]] = {
    val maybeGraffel: ConnectionIO[Option[Entity[Graffel]]] =
      findGraffelByPosition(position)

    val optF: ConnectionIO[Option[ConnectionIO[Entity[Graffel]]]] =
      maybeGraffel.map(opt => opt.map(_.pure[ConnectionIO]))

    val result: ConnectionIO[Entity[Graffel]] =
      optF.flatMap(_.getOrElse(create(Graffel(position))))

    result
  }

  override def findTagsByGraffel(
      id: Id[Graffel]): ConnectionIO[List[Entity[Tag]]] =
    GraffelStatements.findTagsByGraffel(id).list
}
