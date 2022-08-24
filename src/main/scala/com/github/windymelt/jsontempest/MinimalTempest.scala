package com.github.windymelt.jsontempest

import com.github.windymelt.jsontempest.Attr.Attr
import io.circe.Json

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
    val parsedJson = parse(json)
    (parsedSchema, parsedJson) mapN { (s, j) =>
      println("parse successful")
      println(s)
      validateJson(s, j)
    } getOrElse (false)
  }

  private def validateJson(schema: Schema, json: Json): Boolean = {
    // Satisfy all attributes contained in schema.
    val attrs: Set[Attr] = Set() // ???
    attrs.map(_.validateThis(json))
    schema.properties match {
      case None => true
      case Some(p) =>
        p.get("foo") match {
          case None => false
          case Some(foov) =>
            foov.exclusiveMaximum match {
              case None =>
                foov.`type` match {
                  case "string"  => json.asObject.get("foo").get.isString
                  case "integer" => json.asObject.get("foo").get.isNumber
                }
              case Some(max) =>
                val foo = json.asObject.get("foo").get
                println(foo)
                foo.asNumber match {
                  case None        => false
                  case Some(value) => value.toInt.get < max
                }
            }
        }
    }
  }
}
