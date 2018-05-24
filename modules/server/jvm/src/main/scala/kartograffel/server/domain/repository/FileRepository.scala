package kartograffel.server.domain.repository

import cats.Monad
import cats.data.OptionT
import fs2.Stream
import kartograffel.shared.domain.model.{File, FileF, User}
import kartograffel.shared.model.{Entity, Graffel, Id}

trait FileRepository[F[_]] extends EntityRepository[F, File] {
  def attach(file: File, graffelId: Id[Graffel]): F[Entity[File]]

  def findByGraffel(id: Id[Graffel]): Stream[F, Entity[File]]

  def findF[A](id: Id[File], f: Id[User] => F[Option[A]])(
      implicit F: Monad[F]): F[Option[Entity[FileF[A]]]] =
    (for {
      file <- OptionT(findById(id))
      a <- OptionT(f(file.value.uploadedBy))
    } yield Entity(Id(file.id.value), file.value.copy(uploadedBy = a))).value
}
