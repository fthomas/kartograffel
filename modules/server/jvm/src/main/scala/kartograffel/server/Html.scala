package kartograffel.server

object Html {
  val index: String = s"""
    |<!DOCTYPE html>
    |<html>
    |  <head>
    |    <meta charset="UTF-8">
    |  </head>
    |  <body>
    |    <script type="text/javascript" src="${BuildInfo.assetsPath}/client-jsdeps.js"></script>
    |    <script type="text/javascript" src="${BuildInfo.assetsPath}/client-${BuildInfo.jsOptPostfix}.js"></script>
    |  </body>
    |</html>
    """.stripMargin.trim
}
