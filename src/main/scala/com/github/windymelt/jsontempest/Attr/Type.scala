package com.github.windymelt.jsontempest.Attr

import io.circe.Json

import com.github.windymelt.jsontempest.Attr.Attr
import com.github.windymelt.jsontempest.Schema

final case class Type(`type`: String) extends Attr {
  def validateThis(json: Json): Boolean = true
}

object Type extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = Some(Type(s.`type`))
}
