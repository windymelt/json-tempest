package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json
import cats.data.{Validated, Chain}

final case class Not(schema: Schema) extends Attr {
  def validateThis(json: Json) = schema.validate(json).isValid match {
    case true => Validated.invalidNec("should be invalid")
    case false => Validated.valid(())
  }
}

final case object AlwaysValid extends Attr {
  def validateThis(j: Json) = Validated.valid(())
}

final case object AlwaysInvalid extends Attr {
  def validateThis(j: Json) = Validated.invalidNec("Always invalid")
}

object Not extends AttrObject {
  import shapeless._
  def fromSchema(schema: Schema): Option[Attr] = schema.not.map {
    case Inl(s: Schema) => Not(s)
    case Inr(Inl(true)) => AlwaysInvalid
    case Inr(Inl(false)) => AlwaysValid
    case Inr(Inr(_)) => ??? // couldn't reach here
  }
}
