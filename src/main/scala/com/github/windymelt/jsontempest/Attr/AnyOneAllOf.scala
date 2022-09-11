package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json
import cats.implicits._
import cats.data.{Validated, ValidatedNec}
import Validated.valid

final case class SthOf(schemas: Set[Schema], inclusiveMin: Option[Int], inclusiveMax: Option[Int]) extends Attr {
  def validateThis(json: Json): Schema.SchemaValidatedResult = {
    // Convert to Seq to keep its size (validated result may be duplicated)
    val validated0 = schemas.toSeq.map(_.validate(json))
    val validatedSum = validated0.map {
      case Validated.Invalid(_) => 0
      case Validated.Valid(_) => 1
    }.sum
    val underlyingErrors = validated0.reduce(_ *> _) match {
      case Validated.Valid(_) => "[all valid]"
      case Validated.Invalid(es) => es.toChain.toVector.mkString(", ")
    }
    val checkMin: Int => ValidatedNec[String, Unit] =
      inclusiveMin.map(min => (x: Int) => Validated.condNec(min <= x, (), s"should pass at least $min schema ($x / ${validated0.size} passed) ($underlyingErrors)")).getOrElse((_: Int) => valid(()))
    val checkMax: Int => ValidatedNec[String,Unit] =
      inclusiveMax.map(max => (x: Int) => Validated.condNec(x <= max, (), s"should pass at most $max schema ($x / ${validated0.size} passed) ($underlyingErrors)")).getOrElse((_: Int) => valid(()))
    val validated = checkMin(validatedSum) *> checkMax(validatedSum)

    validated
  }
}

final case class AllOfBoolean(bools: Set[Boolean]) extends Attr {
  def validateThis(j: Json) = Validated.condNec(!bools.contains(false), (), "All condition should be true")
}
final case class AnyOfBoolean(bools: Set[Boolean]) extends Attr {
  def validateThis(j: Json) = Validated.condNec(bools.contains(true), (), "At least one condition should be true")
}
final case class OneOfBoolean(bools: Set[Boolean]) extends Attr {
  def validateThis(j: Json) = Validated.condNec(bools.count(_ == true) == 1, (), "Just one condition should be true")
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
