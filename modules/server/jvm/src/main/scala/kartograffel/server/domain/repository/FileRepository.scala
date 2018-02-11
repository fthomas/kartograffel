package kartograffel.server.domain.repository

import kartograffel.shared.domain.model.File

trait FileRepository[F[_]] extends EntityRepository[F, File]
