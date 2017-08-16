import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string._

type Age = Int Refined Interval.ClosedOpen[W.`7`.T, W.`77`.T]

val age = 55

val res: Either[String, Age] = refineV(age)