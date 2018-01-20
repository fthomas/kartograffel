package kartograffel.server.infrastructure.doobie

import java.sql.Timestamp
import java.time._

import doobie.util.meta.Meta

trait DoobieInstances extends doobie.refined.Instances {
  implicit val localDateTimeMeta: Meta[LocalDateTime] =
    Meta[Timestamp].xmap(_.toLocalDateTime, Timestamp.valueOf)
}
