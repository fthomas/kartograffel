package kartograffel.shared.model

final case class Graffel(
    id: Graffel.Id,
    location: Location
)

object Graffel {
  final case class Id(value: String) extends AnyVal
}
