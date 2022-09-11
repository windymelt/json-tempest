package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json
import cats.data.Validated

// TODO: Heterogeneous enum
final case class Enum(cases: Set[Json]) extends Attr {
  def validateThis(json: Json) = Validated.condNec(cases contains json, (), s"Enum violation: ${json}")
}

object Enum extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.`enum`.map(Enum(_))
}
