package com.github.windymelt.jsontempest.Attr

import io.circe.Json

import com.github.windymelt.jsontempest.Attr.Attr
import com.github.windymelt.jsontempest.Schema
import com.github.windymelt.jsontempest.MinimalTempest
import cats.data.Validated

final case class MultipleOf(j: Json) extends Attr {
  def validateThis(json: Json) = {
    (json.asNumber, j.asNumber) match {
      case (Some(num), Some(x)) if x.toDouble >= 1 =>
        Validated.condNec((num.toDouble % x.toDouble) <= Double.MinPositiveValue, (), s"Number ${num} is not multiple of $x")
      case (Some(num), Some(x)) if x.toDouble < 1 =>
        import scala.util.Try
        Try {
          Validated.condNec((num.toBigDecimal.get % x.toBigDecimal.get) == 0, (), s"Number ${num} is not multiple of $x")
        } getOrElse(Validated.invalidNec("Couldn't divide"))
      case (None, _) => Validated.valid(())
      case (_, Some(_)) => Validated.invalidNec(s"$j should be number")
      case _ => Validated.valid(())
    }
  }
}

object MultipleOf extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.multipleOf.map(MultipleOf(_))
}
