package kartograffel.server.usecase

import kartograffel.server.db.GraffelRepository
import kartograffel.shared.model.{Entity, Graffel, Position}
import cats.{Monad, ~>}
import cats.implicits._
import doobie.imports._
import doobie.util.transactor.Transactor
import kartograffel.server.db.GraffelRepository.connectionIo

trait GraffelUseCase[F[_]] { self =>
  val gr: GraffelRepository[F]

  private val monadF = implicitly[Monad[F]]
  private def pure[T](t: T): F[T] = implicitly[Monad[F]].pure(t)

  def findByPositionOrCreate(position: Position): F[Entity[Graffel]] = {
    val maybeGraffel: F[Option[Entity[Graffel]]] =
      gr.findGraffelByPosition(position)

    val optF: F[Option[F[Entity[Graffel]]]] =
      monadF.map(maybeGraffel)(opt => opt.map(pure(_)))

    val result: F[Entity[Graffel]] = monadF.flatMap(optF)(_.getOrElse(gr.insert(Graffel(position))))

    result
  }

  def mapK[G[_]](t: F ~> G): GraffelUseCase[G] = new GraffelUseCase[G] {

    override val gr: GraffelRepository[G] = implicitly[GraffelRepository[G]]

    override def findByPositionOrCreate(position: Position): G[Entity[Graffel]] =
      t(self.findByPositionOrCreate(position))
  }

}

object GraffelUseCase {
  val connectionIO = new GraffelUseCase[ConnectionIO] {
    override val gr: GraffelRepository[ConnectionIO] = implicitly[GraffelRepository[ConnectionIO]]
  }

  def transactional[M[_]: Monad](xa: Transactor[M]): GraffelUseCase[M] =
    connectionIO.mapK(xa.trans)
}
