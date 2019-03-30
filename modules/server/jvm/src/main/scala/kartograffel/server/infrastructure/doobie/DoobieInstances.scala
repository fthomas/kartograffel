package kartograffel.server.infrastructure.doobie

import java.sql.Timestamp
import java.time._
import java.util.UUID

import doobie.util.Meta

trait DoobieInstances extends doobie.refined.Instances {
  implicit val localDateTimeMeta: Meta[LocalDateTime] =
    Meta[Timestamp].imap(_.toLocalDateTime)(Timestamp.valueOf)

  implicit val uuidMeta: Meta[UUID] =
    Meta[String].imap(UUID.fromString)(_.toString)

}
