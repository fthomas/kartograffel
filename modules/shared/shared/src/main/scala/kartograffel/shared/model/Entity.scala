package kartograffel.shared.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

final case class Entity[T](id: Id[T], value: T)

object Entity {
  def from[T](id: Long, value: T): Entity[T] =
    Entity(Id(id), value)

  implicit def entityDecoder[T: Decoder]: Decoder[Entity[T]] =
    deriveDecoder

  implicit def entityEncoder[T: Encoder]: Encoder[Entity[T]] =
    deriveEncoder
}

final case class Id[T](value: Long) extends AnyVal

object Id {
  implicit def idDecoder[T]: Decoder[Id[T]] =
    Decoder.decodeLong.map(Id.apply)

  implicit def idEncoder[T]: Encoder[Id[T]] =
    Encoder.encodeLong.contramap(_.value)
}
