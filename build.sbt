import Dependencies._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.windymelt"
ThisBuild / organizationName := "windymelt"

lazy val root = (project in file("."))
  .settings(
    name := "json-tempest",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser",
      "io.circe" %% "circe-shapes"
    ).map(_ % circeVersion),
    libraryDependencies += "com.chuusai" % "shapeless_2.13" % "2.3.9"
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
