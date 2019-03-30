package kartograffel.server.domain.model

sealed trait DistanceUnit
case object meter extends DistanceUnit
case object kilometer extends DistanceUnit
