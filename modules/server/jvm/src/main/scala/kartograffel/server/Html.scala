package kartograffel.server

object Html {
  val index: String = s"""
    |<!DOCTYPE html>
    |<html>
    |  <head>
    |    <meta charset="UTF-8">
    |    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.4.0/leaflet.css"crossorigin=""/>
    |    <script src="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.4.0/leaflet.js" crossorigin=""></script>
    |  </head>
    |  <body>
    |   <div id="app"></div>
    |   <script type="text/javascript" src="${BuildInfo.assetsPath}/client-jsdeps.js"></script>
    |   <script type="text/javascript" src="${BuildInfo.assetsPath}/client-${BuildInfo.jsOptPostfix}.js"></script>
    |  </body>
    |</html>
    """.stripMargin.trim
}
