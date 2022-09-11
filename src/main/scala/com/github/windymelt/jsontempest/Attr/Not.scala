package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json

final case class Not(schema: Schema) extends Attr {
  def validateThis(json: Json): Boolean = !schema.validate(json)
}

object Not extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.not.map(Not(_))
}
