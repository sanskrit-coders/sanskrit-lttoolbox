name := "sanskrit-lttoolbox"

scalaVersion := "2.12.6"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

val akkaVersion = "2.5.16"
val scalactestVersion = "3.0.5"
val logbackVersion = "1.2.3"
val json4sVersion = "3.6.1"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % logbackVersion
  ,"ch.qos.logback" % "logback-core" % logbackVersion
  ,"org.json4s" % "json4s-ast_2.12" % json4sVersion
  ,"org.json4s" % "json4s-native_2.12" % json4sVersion
  ,"org.apache.commons" % "commons-csv" % "1.5"
  ,"com.typesafe.akka" % "akka-actor_2.12" % akkaVersion
  ,"com.typesafe.akka" % "akka-testkit_2.12" % akkaVersion % "test"
  ,"com.github.sanskrit-coders" % "indic-transliteration_2.12" % "1.30"
  ,"com.github.sanskrit-coders" % "db-interface_2.12" % "3.1"
)

libraryDependencies += "org.scalactic" %% "scalactic" % scalactestVersion
libraryDependencies += "org.scalatest" %% "scalatest" % scalactestVersion % "test"

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scmInfo := Some(
  ScmInfo(
    url("https://github.com/sanskrit-coders/sanskrit-lttoolbox"),
    "scm:git@github.com:sanskrit-coders/sanskrit-lttoolbox.git"
  )
)

useGpg := true
publishMavenStyle := true
publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommand("publishSigned"),
  setNextVersion,
  commitNextVersion,
  releaseStepCommand("sonatypeReleaseAll"),
  pushChanges
)
