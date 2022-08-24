package com.github.windymelt.jsontempest

import com.github.windymelt.jsontempest.Attr.Attr

trait MinimalTempest {
  case class Foo(foo: Int)

  /** Very primitive stub validate function.
    *
    * @return
    */
  def validate(schema: String)(json: String): Boolean = {
    import io.circe._
    import io.circe.generic.auto._
    import io.circe.parser._
    import io.circe.syntax._
    import cats._
    import cats.implicits.catsSyntaxTuple2Semigroupal

    // parse json
    // foo should be integer
    val parsedSchema = Schema.fromString(schema)
    val parsedJson = decode[Foo](json)
    (parsedSchema, parsedJson) mapN { (s, j) =>
      println("parse successful")
      println(s)
      validateJson(s, j)
    } getOrElse (false)
  }

  private def validateJson(schema: Schema, json: Foo): Boolean = {
    true
  }
}
