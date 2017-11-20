name := "sanskrit-lttoolbox"
version := "0.6"

scalaVersion := "2.12.3"

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "0.9.29"
  ,"ch.qos.logback" % "logback-core" % "0.9.29"
  ,"org.json4s" % "json4s-ast_2.12" % "3.5.2"
  ,"org.json4s" % "json4s-native_2.12" % "3.5.2"
  ,"org.apache.commons" % "commons-csv" % "1.4"
  ,"com.typesafe.akka" % "akka-actor_2.12.0-M5" % "2.4.8"
  ,"com.github.sanskrit-coders" % "indic-transliteration_2.12" % "1.8"
  ,"com.github.sanskrit-coders" % "db-interface_2.12" % "2.9"
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

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
