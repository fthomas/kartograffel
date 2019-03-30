package kartograffel.server.infrastructure.doobie.service
import kartograffel.server.domain.service.GraffelService
import kartograffel.server.infrastructure.doobie.repository.{DbGraffelRepository, DbTagRepository}
import kartograffel.server.infrastructure.doobie.util.DbUUIDUtil

object DbGraffelService extends GraffelService(DbGraffelRepository, DbTagRepository, DbUUIDUtil)
