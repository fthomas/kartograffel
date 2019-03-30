package kartograffel.server.domain.util
import java.util.UUID

import cats.effect.Sync

trait UUIDUtil[F[_]] {
  def uuid(implicit F: Sync[F]): F[UUID] = F.delay(UUID.randomUUID)
}
