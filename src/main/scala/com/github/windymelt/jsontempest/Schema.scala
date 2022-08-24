package com.github.windymelt.jsontempest

import Attr.Attr
import io.circe.generic.auto._, io.circe.syntax._

case class Schema(
    `$schema`: Option[String] = None,
    `$id`: Option[String] = None,
    title: Option[String] = None,
    description: Option[String] = None,
    `type`: String,
    properties: Option[Map[String, Schema]] = None,
    exclusiveMaximum: Option[Int] = None
)

object Schema {
  def fromString(s: String): Either[io.circe.Error, Schema] = {
    import io.circe._
    import io.circe.parser._
    import io.circe.syntax._
    decode[Schema](s)
  }
}
