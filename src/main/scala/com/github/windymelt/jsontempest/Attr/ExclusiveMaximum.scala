package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json

final case class ExclusiveMaximum(max: Int) extends Attr {
  def validateThis(json: Json): Boolean = {
    println("validating exclusiveMaximum");
    {
      for {
        num <- json.asNumber
      } yield num.toDouble < max
    } getOrElse (false)
  }
}

object ExclusiveMaximum extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] =
    s.exclusiveMaximum.map(ExclusiveMaximum(_))
}
