import sbtcrossproject.{crossProject, CrossProject}

/// variables

val projectName = "kartograffel"
val rootPkg = "kartograffel"

val circeVersion = "0.11.1"
val doobieVersion = "0.6.0"
val flywayVersion = "5.2.4"
val fs2Version = "1.0.4"
val h2Version = "1.4.197"
val http4sVersion = "0.20.1"
val logbackVersion = "1.2.3"
val refinedVersion = "0.9.2"
val scalacheckShapelessVersion = "1.2.2"
val scalajsJqueryVersion = "0.9.5"
val scalajsReactVersion = "1.4.1"
val scalaTestVersion = "3.0.7"
val specs2Version = "4.4.1"
val webjarJqueryVersion = "3.3.1"
val webjarReactVersion = "16.7.0"

/// projects

lazy val root = project
  .in(file("."))
  .aggregate(clientJS)
  .aggregate(serverJVM)
  .aggregate(sharedJS)
  .aggregate(sharedJVM)
  .settings(commonSettings)
  .settings(noPublishSettings)

lazy val client = crossProject(JSPlatform)
  .configureCross(moduleCrossConfig("client"))
  .jsConfigure(_.dependsOn(sharedJS))
  .enablePlugins(ScalaJSWeb)
  .settings(
    // Disabling coverage because of:
    // https://github.com/fthomas/kartograffel/pull/5
    coverageEnabled := false,
    libraryDependencies ++= Seq(
      "be.doeraene" %%% "scalajs-jquery" % scalajsJqueryVersion,
      "co.fs2" %%% "fs2-core" % fs2Version,
      //scalajs-react
      "com.github.japgolly.scalajs-react" %%% "core" % scalajsReactVersion,
      "com.github.japgolly.scalajs-react" %%% "extra" % scalajsReactVersion,
      "com.github.japgolly.scalajs-react" %%% "ext-cats" % scalajsReactVersion,
      /// test dependencies
      "com.github.japgolly.scalajs-react" %%% "test" % scalajsReactVersion % Test,
      // Replace with specs2 when it supports Scala.js:
      // https://github.com/etorreborre/specs2/issues/465
      "org.scalatest" %%% "scalatest" % scalaTestVersion % Test
    ),
    jsEnv in Test := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),
    jsDependencies ++= Seq(
      "org.webjars" % "jquery" % webjarJqueryVersion / s"$webjarJqueryVersion/jquery.js",
      "org.webjars.npm" % "react" % webjarReactVersion
        / "umd/react.development.js"
        minified "umd/react.production.min.js"
        commonJSName "React",
      "org.webjars.npm" % "react-dom" % webjarReactVersion
        / "umd/react-dom.development.js"
        minified "umd/react-dom.production.min.js"
        dependsOn "umd/react.development.js"
        commonJSName "ReactDOM"
    ),
    dependencyOverrides ++= Seq(
      "org.webjars.npm" % "js-tokens" % "4.0.0",
      "org.webjars.npm" % "scheduler" % "0.11.0"
    ),
    scalaJSUseMainModuleInitializer := true
  )

lazy val clientJS = client.js

lazy val server = crossProject(JVMPlatform)
  .configureCross(moduleCrossConfig("server"))
  .jvmConfigure(_.dependsOn(sharedJVM % "compile->compile;test->test"))
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(DebianPlugin, JavaServerAppPackaging, SystemVPlugin)
  .enablePlugins(SbtWeb)
  .settings(
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "com.h2database" % "h2" % h2Version,
      "eu.timepit" %% "refined" % refinedVersion,
      "eu.timepit" %% "refined-pureconfig" % refinedVersion,
      "org.flywaydb" % "flyway-core" % flywayVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.tpolecat" %% "doobie-refined" % doobieVersion,
      /// test dependencies
      "org.http4s" %% "http4s-testing" % http4sVersion % Test,
      "org.specs2" %% "specs2-core" % specs2Version % Test,
      "org.tpolecat" %% "doobie-specs2" % doobieVersion % Test
    )
  )
  // command line options for run and reStart
  .settings(
    configDirectory := sourceDirectory.in(Universal).value / "conf",
    configFile := configDirectory.value / "application.conf",
    fork.in(run) := true,
    fork.in(Test) := true,
    javaOptions ++= {
      Seq(
        s"-Dconfig.file=${configFile.value}",
        s"-Dlogback.configurationFile=/logback.xml"
      )
    }
  )
  // sbt-buildinfo settings
  .settings(
    buildInfoKeys := {
      val assetsRoot = "assetsRoot" -> "assets"
      val assetsPath = "assetsPath" -> s"${assetsRoot._2}/${moduleName.value}/${version.value}"
      Seq[BuildInfoKey](
        name,
        version,
        moduleName,
        modulePkg,
        assetsRoot,
        assetsPath,
        crossTarget,
        BuildInfoKey.map(isDevMode.in(scalaJSPipeline)) {
          case (_, value) =>
            "jsOptPostfix" -> (if (value) "fastopt" else "opt")
        }
      )
    },
    buildInfoPackage := modulePkg.value
  )
  // sbt-heroku settings
  .settings(
    herokuAppName in Compile := name.value
  )
  // sbt-native-packager settings
  .settings(
    bashScriptExtraDefines ++= Seq(
      """addJava "-Dconfig.file=${app_home}/../conf/application.conf"""",
      """addJava "-Dlogback.configurationFile=${app_home}/../conf/logback.xml""""
    )
  )
  // sbt-web-scalajs settings
  .settings(
    scalaJSProjects := Seq(clientJS),
    pipelineStages.in(Assets) := Seq(scalaJSPipeline)
  )

lazy val serverJVM = server.jvm

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .configureCross(moduleCrossConfig("shared"))
  .settings(
    libraryDependencies ++= Seq(
      "eu.timepit" %%% "refined" % refinedVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-refined" % circeVersion,
      "io.circe" %%% "circe-java8" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      /// test dependencies
      "com.github.alexarchambault" %%% "scalacheck-shapeless_1.14" % scalacheckShapelessVersion % Test,
      "eu.timepit" %%% "refined-scalacheck" % refinedVersion % Test,
      "io.circe" %%% "circe-testing" % circeVersion % Test,
      "org.scalatest" %%% "scalatest" % scalaTestVersion % Test
    )
  )

lazy val sharedJS = shared.js
lazy val sharedJVM = shared.jvm

/// settings

def moduleCrossConfig(name: String): CrossProject => CrossProject =
  _.in(file(s"modules/$name"))
    .settings(
      moduleName := name,
      modulePkg := s"$rootPkg.$name"
    )
    .settings(commonSettings)

lazy val commonSettings = Def.settings(
  compileSettings,
  consoleSettings,
  metadataSettings,
  packageSettings
)

lazy val compileSettings = Def.settings(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xfuture",
    "-Xlint:-unused,_",
    "-Yno-adapted-args",
    "-Ywarn-unused:implicits",
    "-Ywarn-unused:imports",
    "-Ywarn-unused:locals",
    "-Ywarn-unused:patvars",
    "-Ywarn-unused:privates",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"
  )
)

lazy val consoleSettings = Def.settings(
  scalacOptions in (Compile, console) -= "-Ywarn-unused:imports",
  scalacOptions in (Test, console) := (scalacOptions in (Compile, console)).value,
  initialCommands += s"""
    import eu.timepit.refined.auto._
    import $rootPkg.shared._
    import $rootPkg.shared.model._
    import $rootPkg.shared.domain.model._
    import ${modulePkg.value}._
  """,
  initialCommands in Test += s"""
    import org.scalacheck.Arbitrary
    import $rootPkg.shared.model.ArbitraryInstances._
  """
)

lazy val metadataSettings = Def.settings(
  name := projectName,
  licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0")),
  startYear := Some(2017)
)

lazy val packageSettings = Def.settings(
  maintainer := s"$projectName author(s) <$projectName@example.org>"
)

lazy val noPublishSettings = Def.settings(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

/// taskKeys and settingKeys

lazy val h2Console = taskKey[Unit]("Runs the H2 console.")
h2Console := {
  val cpFiles = managedClasspath.in(Compile).in(serverJVM).value.files
  val h2jar = cpFiles.find(_.toString.contains(h2Version))

  val cfgFile = configFile.in(serverJVM).value
  val log = streams.value.log

  h2jar.fold {
    sys.error(s"Could not find H2 JAR for version $h2Version")
  } { h2File =>
    import com.typesafe.config.ConfigFactory
    val cfg = ConfigFactory.parseFile(cfgFile)

    val command = Seq(
      "java",
      "-jar",
      h2File.toString,
      "-url",
      cfg.getString("db.url"),
      "-driver",
      cfg.getString("db.driver"),
      "-user",
      cfg.getString("db.user"),
      "-password",
      cfg.getString("db.password")
    )
    log.info(s"Running ${command.mkString(" ")}")
    scala.sys.process.Process(command).run()
  }
}

lazy val configDirectory = settingKey[File]("")

lazy val configFile = settingKey[File]("")

lazy val modulePkg = settingKey[String]("")
modulePkg := rootPkg

/// commands

def addCommandsAlias(name: String, cmds: Seq[String]) =
  addCommandAlias(name, cmds.mkString(";", ";", ""))

addCommandsAlias(
  "validate",
  Seq(
    "clean",
    "scalafmtCheck",
    "scalafmtSbtCheck",
    "test:scalafmtCheck",
    "coverageOn",
    "test",
    "coverageReport",
    "coverageOff",
    "doc",
    "package",
    "packageSrc",
    "debian:packageBin"
  )
)

addCommandsAlias("deployHerokuCmds",
                 Seq(
                   "clean",
                   "serverJVM/stage",
                   "serverJVM/deployHeroku"
                 ))
