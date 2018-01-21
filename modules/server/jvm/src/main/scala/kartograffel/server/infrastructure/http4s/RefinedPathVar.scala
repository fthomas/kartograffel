package kartograffel.server.infrastructure.http4s

import eu.timepit.refined.api.RefinedType
import eu.timepit.refined.types.numeric.NonNegLong

import scala.util.Try

class RefinedPathVar[FTP, T](parse: String => T)(
    implicit rt: RefinedType.AuxT[FTP, T]) {

  def unapply(arg: String): Option[FTP] =
    Try(rt.unsafeRefine(parse(arg))).toOption
}

object NonNegLongVar extends RefinedPathVar[NonNegLong, Long](_.toLong)
