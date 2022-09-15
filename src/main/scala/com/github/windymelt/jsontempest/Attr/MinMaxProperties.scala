package com.github.windymelt.jsontempest
  package Attr

import cats.data.Validated
import io.circe.Json
import cats.implicits._

final case class MinMaxProperties(inclusiveMin: Option[Int], inclusiveMax: Option[Int]) extends Attr {
  def validateThis(json: Json): Schema.SchemaValidatedResult = {
    json.asObject match {
      case None => Validated.valid()
      case Some(jo) =>
        val checkMin = inclusiveMin.map(n => Validated.condNec(jo.size >= n, (), s"Object should have at least $n properties")).getOrElse(Validated.valid(()))
        val checkMax = inclusiveMax.map(n => Validated.condNec(jo.size <= n, (), s"Object should have at most $n properties")).getOrElse(Validated.valid(()))
        checkMin *> checkMax
    }
  }
}

object MinProperties extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.minProperties map (m => MinMaxProperties(m.some, None))
}

object MaxProperties extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.maxProperties map (m => MinMaxProperties(None, m.some))
}
