package com.github.windymelt
package jsontempest

import Attr.Attr
import io.circe.generic.auto._, io.circe.generic.semiauto._, io.circe.syntax._, io.circe.Json
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
    not: Option[Schema :+: Boolean :+: CNil] = None,
  required: Option[Set[String]] = None,
    minimum: Option[Double] = None,
    maximum: Option[Double] = None,
    exclusiveMinimum: Option[Double] = None,
    exclusiveMaximum: Option[Double] = None,
    allOf: Option[Set[Schema] :+: Set[Boolean] :+: CNil] = None,
) {
  def validate(json: Json): Boolean = {
    val attrs: Set[Attr] =
      Schema.allAttrs flatMap (_.fromSchema(this))
    val validated = attrs.map(_.validateThis(json))
    validated match {
      case e if e.isEmpty => true
      case otherwise => validated.reduce(_ && _)
    }
  }
}

object Schema {
  import Attr._
  val allAttrs = Set(Properties, Type, Enum, Minimum, ExclusiveMinimum, Maximum, ExclusiveMaximum, Not, Required, AllOf)
  def fromString(s: String): Either[io.circe.Error, Schema] = {
    import io.circe._
    import io.circe.parser._
    import io.circe.syntax._
    decode[Schema](s)
  }
  // for external project
  import io.circe.Decoder
  implicit val SchemaDecoder: Decoder[Schema] = deriveDecoder
}
