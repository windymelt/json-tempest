package com.github.windymelt.jsontempest

import Attr.Attr
import io.circe.generic.auto._, io.circe.syntax._

case class Schema(
    `$schema`: Option[String],
    `$id`: Option[String],
    title: Option[String],
    description: Option[String],
    `type`: String,
    properties: Option[Map[String, Schema]]
)

object Schema {
  def fromString(s: String): Either[io.circe.Error, Schema] = {
    import io.circe._
    import io.circe.parser._
    import io.circe.syntax._
    decode[Schema](s)
  }
}
