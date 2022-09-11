package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json

final case class SthOf(schemas: Set[Schema], inclusiveMin: Option[Int], inclusiveMax: Option[Int]) extends Attr {
  def validateThis(json: Json): Boolean = {
    val validatedSum = schemas.map(_.validate(json) match {case false => 0; case true => 1}).sum
    val checkMin = inclusiveMin.map(min => (x: Int) => min <= x).getOrElse((_: Int) => true)
    val checkMax = inclusiveMax.map(max => (x: Int) => x <= max).getOrElse((_: Int) => true)
    checkMin(validatedSum) && checkMax(validatedSum)
  }
}

final case class AllOfBoolean(bools: Set[Boolean]) extends Attr {
  def validateThis(j: Json): Boolean = !bools.contains(false)
}
final case class AnyOfBoolean(bools: Set[Boolean]) extends Attr {
  def validateThis(j: Json): Boolean = bools.contains(true)
}
final case class OneOfBoolean(bools: Set[Boolean]) extends Attr {
  def validateThis(j: Json): Boolean = bools.count(_ == true) == 1
}

object AllOf extends AttrObject {
  import shapeless._
  def fromSchema(schema: Schema): Option[Attr] = schema.allOf.map {
    case Inl(s: Set[Schema]) => SthOf(s, Some(s.size), Some(s.size))
    case Inr(Inl(bs: Set[Boolean])) => AllOfBoolean(bs)
    case Inr(Inr(_)) => ??? // Shouldn't be reached
  }
}

object AnyOf extends AttrObject {
  import shapeless._
  def fromSchema(schema: Schema): Option[Attr] = schema.anyOf.map {
    case Inl(s: Set[Schema]) => SthOf(s, Some(1), None)
    case Inr(Inl(bs: Set[Boolean])) => AnyOfBoolean(bs)
    case Inr(Inr(_)) => ??? // Shouldn't be reached
  }
}

object OneOf extends AttrObject {
  import shapeless._
  def fromSchema(schema: Schema): Option[Attr] = schema.oneOf.map {
    case Inl(s: Set[Schema]) => SthOf(s, Some(1), Some(1))
    case Inr(Inl(bs: Set[Boolean])) => OneOfBoolean(bs)
    case Inr(Inr(_)) => ??? // Shouldn't be reached
  }
}
