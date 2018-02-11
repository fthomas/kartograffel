package kartograffel.shared.domain.model

import java.time.LocalDateTime

final case class File(
    mimeType: String,
    uploadedAt: LocalDateTime,
    content: Array[Byte]
)
