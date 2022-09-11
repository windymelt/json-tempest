package com.github.windymelt.jsontempest.Attr

import io.circe.Json

import com.github.windymelt.jsontempest.Attr.Attr
import com.github.windymelt.jsontempest.Schema
import com.github.windymelt.jsontempest.MinimalTempest

final case class Required(requiredFields: Set[String]) extends Attr {
  def validateThis(json: Json): Boolean = {
    json.asObject match {
      case None => false
      case Some(jo) =>
        requiredFields.map(jo.apply(_)).find(_.isEmpty).isEmpty
    }
  }
}

object Required extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.required.map(Required(_))
}
