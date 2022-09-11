package com.github.windymelt.jsontempest
package validator

import scala.io.Source

object Main extends MinimalTempest with App {
  val suitesList = Seq(Source.fromFile("./testsuite/tests/latest/enum.json")) // TODO: walk directory

  for {
    suites <- suitesList
  } yield validateSuites(suites)

  def validateSuites(suitesSource: Source): Unit = {
    import io.circe._
    import io.circe.parser._
    import io.circe.syntax._
    import io.circe.generic.auto._, io.circe.syntax._

    val Right(suites: Seq[TestSuite]) = decode[Seq[TestSuite]](suitesSource.mkString)

    for {
      suite <- suites
    } yield validateSuite(suite)
  }

  def validateSuite(suite: TestSuite): Unit = {
    println(s"[${suite.description}]")
    val schema: Schema = suite.schema
    println(s"schema: ${suite.schema}")
    for {
      test <- suite.tests
    } yield {
      val passed = schema.validate(test.data) == test.valid
      passed match {
        case true => println(s"Passed: ${test.description}")
        case false =>
          println(s"FAILED: ${test.description}")
          println(s"  expected: ${test.valid}")
          println(s"  data:     ${test.data}")
      }
    }
    println()
  }
}
