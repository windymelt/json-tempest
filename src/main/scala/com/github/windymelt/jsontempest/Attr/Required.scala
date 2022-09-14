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
        val lacks = requiredFields.map(k => jo.apply(k).toRight(k)).collect { case Left(x) => x }
        val lacksStr = lacks.mkString(",")
        Validated.condNec(lacks.isEmpty, (), s"Required field ${lacksStr} is missing")
    }
  }
}

object Required extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.required.map(Required(_))
}
