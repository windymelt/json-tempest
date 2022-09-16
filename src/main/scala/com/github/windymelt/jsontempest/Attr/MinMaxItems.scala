package com.github.windymelt.jsontempest
  package Attr

import cats.data.Validated
import io.circe.Json
import cats.implicits._

final case class MinMaxItems(inclusiveMin: Option[Int], inclusiveMax: Option[Int]) extends Attr {
  def validateThis(json: Json): Schema.SchemaValidatedResult = {
    json.asArray match {
      case None => ().validNec
      case Some(ja) =>
        val checkMin = inclusiveMin.map(n => Validated.condNec(ja.size >= n, (), s"Array should have at least $n items")).getOrElse(().valid)
        val checkMax = inclusiveMax.map(n => Validated.condNec(ja.size <= n, (), s"Array should have at most $n items")).getOrElse(().valid)
        checkMin *> checkMax
    }
  }
}

object MinItems extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.minItems map (m => MinMaxItems(m.some, None))
}

object MaxItems extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.maxItems map (m => MinMaxItems(None, m.some))
}

