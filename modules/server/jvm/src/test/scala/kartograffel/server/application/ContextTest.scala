package kartograffel.server.application

import cats.effect.IO
import org.specs2.mutable.Specification

class ContextTest extends Specification {
  "prepare produces a value" >> {
    val maybeCtx = Context.prepare[IO].compile.last.unsafeRunSync()
    maybeCtx.nonEmpty
  }
}
