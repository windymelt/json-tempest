package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json

final case class AllOf(schemas: Set[Schema]) extends Attr {
  def validateThis(json: Json): Boolean = schemas.map(_.validate(json)).reduce(_ && _)
}

final case class AllOfBoolean(bools: Set[Boolean]) extends Attr {
  def validateThis(j: Json): Boolean = !bools.contains(false)
}

object AllOf extends AttrObject {
  import shapeless._
  def fromSchema(schema: Schema): Option[Attr] = schema.allOf.map {
    case Inl(s: Set[Schema]) => AllOf(s)
    case Inr(Inl(bs: Set[Boolean])) => AllOfBoolean(bs)
    case Inr(Inr(_)) => ??? // Shouldn't be reached
  }
}
