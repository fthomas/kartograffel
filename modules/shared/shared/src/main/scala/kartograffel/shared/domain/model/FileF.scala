package kartograffel.shared.domain.model

import java.time.LocalDateTime

final case class FileF[A](
    mimeType: String,
    uploadedAt: LocalDateTime,
    uploadedBy: A,
    content: Array[Byte]
)
