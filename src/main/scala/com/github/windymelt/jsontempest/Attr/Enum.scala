package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json

// TODO: Heterogeneous enum
final case class Enum(cases: Set[Json]) extends Attr {
  def validateThis(json: Json): Boolean = cases contains json
}

object Enum extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.`enum`.map(Enum(_))
}
