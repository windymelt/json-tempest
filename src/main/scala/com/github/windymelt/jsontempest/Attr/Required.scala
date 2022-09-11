package com.github.windymelt.jsontempest.Attr

import io.circe.Json

import com.github.windymelt.jsontempest.Attr.Attr
import com.github.windymelt.jsontempest.Schema
import com.github.windymelt.jsontempest.MinimalTempest
import cats.data.Validated

final case class Required(requiredFields: Set[String]) extends Attr {
  def validateThis(json: Json) = {
    json.asObject match {
      case None => Validated.invalidNec(s"$json should be object")
      case Some(jo) =>
        Validated.condNec(requiredFields.map(jo.apply(_)).find(_.isEmpty).isEmpty, (), "Required field is missing")
    }
  }
}

object Required extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.required.map(Required(_))
}
