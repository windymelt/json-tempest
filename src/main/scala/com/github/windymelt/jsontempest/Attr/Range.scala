package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json

sealed trait InclusiveOrExclusive[A]
final case class Inclusive[A](x: A) extends InclusiveOrExclusive[A]
final case class Exclusive[A](x: A) extends InclusiveOrExclusive[A]

final case class Range[A <% Double](min: Option[InclusiveOrExclusive[A]], max: Option[InclusiveOrExclusive[A]]) extends Attr {
  def validateThis(json: Json): Boolean = {
    {
      for {
        num <- json.asNumber
      } yield (min, max) match {
        case (None, None) => true
        case (None, Some(ma)) => ma match {
	  case Inclusive(m) => num.toDouble <= m
          case Exclusive(m) => num.toDouble < m
        }
        case (Some(mi), None) => mi match {
          case Inclusive(m) => m <= num.toDouble
          case Exclusive(m) => m < num.toDouble
        }
        case (Some(mi), Some(ma)) => (mi, ma) match {
	  case (Inclusive(mi), Inclusive(ma)) => mi <= num.toDouble && num.toDouble <= ma
	  case (Inclusive(mi), Exclusive(ma)) => mi <= num.toDouble && num.toDouble < ma
	  case (Exclusive(mi), Inclusive(ma)) => mi < num.toDouble && num.toDouble <= ma
	  case (Exclusive(mi), Exclusive(ma)) => mi < num.toDouble && num.toDouble < ma
        }
      }
    } getOrElse (true)
  }
}

object Minimum extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] =
    s.minimum.map(x => Range(Some(Inclusive(x)), None))
}
object ExclusiveMinimum extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] =
    s.exclusiveMinimum.map(x => Range(Some(Exclusive(x)), None))
}
object Maximum extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] =
    s.maximum.map(x => Range(None, Some(Inclusive(x))))
}
object ExclusiveMaximum extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] =
    s.exclusiveMaximum.map(x => Range(None, Some(Exclusive(x))))
}
