package kartograffel.shared.model

import cats.Eq
import cats.syntax.eq._
import eu.timepit.refined.types.numeric.NonNegLong
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import io.circe.refined._

final case class Entity[T](id: Id[T], value: T)

object Entity {
  implicit def entityEq[T: Eq]: Eq[Entity[T]] =
    Eq.instance((x, y) => x.id === y.id && x.value === y.value)

  implicit def entityDecoder[T: Decoder]: Decoder[Entity[T]] =
    deriveDecoder

  implicit def entityEncoder[T: Encoder]: Encoder[Entity[T]] =
    deriveEncoder
}

final case class Id[T](value: NonNegLong)

object Id {
  implicit def idEq[T]: Eq[Id[T]] =
    Eq.fromUniversalEquals

  implicit def idDecoder[T]: Decoder[Id[T]] =
    deriveDecoder

  implicit def idEncoder[T]: Encoder[Id[T]] =
    deriveEncoder
}
