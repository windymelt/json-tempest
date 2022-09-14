package com.github.windymelt.jsontempest
package validator

import scala.io.Source
import cats.data.Validated.Invalid
import cats.data.Validated.Valid
import io.circe.DecodingFailure
import scala.collection.immutable

object Main extends MinimalTempest with App {
  val suitesList = Seq(Source.fromFile("../testsuite/tests/latest/oneOf.json")) // TODO: walk directory
  var hasFailed: Boolean = false

  for {
    suites <- suitesList
  } yield validateSuites(suites)

  def validateSuites(suitesSource: Source): Unit = {
    import io.circe._
    import io.circe.parser._
    import io.circe.syntax._
    import io.circe.generic.auto._, io.circe.syntax._

    decode[Seq[TestSuite]](suitesSource.mkString) match {
      case Left(err) =>
        hasFailed = true
        err match {
	  case e: DecodingFailure =>
            showDecodeError(e)
	  case ParsingFailure(message, underlying) => println(message)
        }
      case Right(suites) =>
        for {
        suite <- suites
        } yield validateSuite(suite)
    }
  }

  private def showDecodeError(e: DecodingFailure): Unit = {
    import io.circe.CursorOp
    println(e.message)
    println(s"Decoding failure occurred at: ${CursorOp.opsToPath(e.history)}")
  }

  def validateSuite(suite: TestSuite): Unit = {
    println(s"\n[${suite.description}]")
    val schema: Schema = suite.schema
//    println(s"schema: ${suite.schema}")
    for {
      test <- suite.tests
    } yield {
      val validated = schema.validate(test.data)
      val passed = validated.isValid == test.valid
      passed match {
        case true => println(s"Passed: ${test.description}")
        case false =>
          println(s"FAILED: ${test.description}")
          println(s"  expected: ${test.valid}")
          println(s"  data:     ${test.data}")
          validated match {
	    case Invalid(e) =>
              println("  violation info:")
              println(e.toChain.toVector.mkString("\n"))
	    case Valid(a) => // nop
          }
          hasFailed = true
      }
    }
    println()
  }

  sys.exit(hasFailed match { case true => 1; case false => 0 })
}
