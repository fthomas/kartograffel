package kartograffel.shared.domain.model

import java.time.LocalDateTime

final case class FileF[A](
    graffel: A,
    mimeType: String,
    createdAt: LocalDateTime,
    content: Array[Byte]
)
