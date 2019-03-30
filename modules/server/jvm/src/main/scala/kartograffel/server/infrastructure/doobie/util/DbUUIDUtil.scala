package kartograffel.server.infrastructure.doobie.util
import doobie.free.connection.ConnectionIO
import kartograffel.server.domain.util.UUIDUtil

object DbUUIDUtil extends UUIDUtil[ConnectionIO]
