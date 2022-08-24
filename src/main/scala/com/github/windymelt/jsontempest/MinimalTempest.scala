package com.github.windymelt.jsontempest

trait MinimalTempest {

  /** Very primitive stub validate function.
    *
    * @return
    */
  def validate(schema: String)(json: String): Boolean = {
    import io.circe._
    import io.circe.generic.auto._
    import io.circe.parser._
    import io.circe.syntax._

    // parse json
    // foo should be integer
    case class Foo(foo: Int)
    val parsedJson = decode[Foo](json)
    parsedJson match {
      case Left(value)  => false
      case Right(value) => true
    }
  }
}
