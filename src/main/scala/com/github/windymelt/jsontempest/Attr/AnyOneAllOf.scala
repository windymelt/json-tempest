package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json
import cats.implicits._
import cats.data.{Validated, ValidatedNec}
import Validated.valid

final case class SthOf(schemas: Set[Schema], inclusiveMin: Option[Int], inclusiveMax: Option[Int]) extends Attr {
  def validateThis(json: Json): Boolean = {
    val validatedSum = schemas.map(_.validate(json) match {case false => 0; case true => 1}).sum
    val checkMin: Int => ValidatedNec[String, Unit] =
      inclusiveMin.map(min => (x: Int) => Validated.condNec(min <= x, (), s"should pass at least $min schema")).getOrElse((_: Int) => valid(()))
    val checkMax: Int => ValidatedNec[String,Unit] =
      inclusiveMax.map(max => (x: Int) => Validated.condNec(x <= max, (), s"should pass at most $max schema")).getOrElse((_: Int) => valid(()))
    val validated = checkMin(validatedSum) *> checkMax(validatedSum)

    if (validated.isInvalid) {
      validated.leftMap(nec => println(s"! ${nec.mkString_("\n")}"))
    }

    validated.isValid
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
