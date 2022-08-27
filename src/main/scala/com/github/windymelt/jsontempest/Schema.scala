package com.github.windymelt
package jsontempest

import Attr.Attr
import io.circe.generic.auto._, io.circe.syntax._, io.circe.Json
import io.circe.shapes._
import shapeless._

case class Schema(
    `$schema`: Option[String] = None,
    `$id`: Option[String] = None,
    title: Option[String] = None,
    description: Option[String] = None,
    `type`: Option[String :+: Set[String] :+: CNil],
    properties: Option[Map[String, Schema]] = None,
    `enum`: Option[Set[Json]] = None,
    exclusiveMaximum: Option[Int] = None
) {
  def validate(json: Json): Boolean = {
    val attrs: Set[Attr] =
      Schema.allAttrs flatMap (_.fromSchema(this))
    println(s"attrs: $attrs")
    val validated = attrs.map(_.validateThis(json))
    println(validated)
    validated.reduce(_ && _)
  }
}

object Schema {
  import Attr._
  val allAttrs = Set(Properties, Type, Enum, ExclusiveMaximum)
  def fromString(s: String): Either[io.circe.Error, Schema] = {
    import io.circe._
    import io.circe.parser._
    import io.circe.syntax._
    decode[Schema](s)
  }
}
