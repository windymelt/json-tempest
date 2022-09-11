package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json
import cats.data.Validated

sealed trait InclusiveOrExclusive[A]
final case class Inclusive[A](x: A) extends InclusiveOrExclusive[A]
final case class Exclusive[A](x: A) extends InclusiveOrExclusive[A]

final case class Range[A <% Double](min: Option[InclusiveOrExclusive[A]], max: Option[InclusiveOrExclusive[A]]) extends Attr {
  def validateThis(json: Json) = {
    import cats.implicits._
    (for {
      num <- json.asNumber
    } yield {
      def checkMin(x: Double): Schema.SchemaValidatedResult = min.map {
        case Inclusive(m) => check((m <= x) -> s"$num should be less or equal than $m")
        case Exclusive(m) => check((m < x) -> s"$num should be less than $m")
      } getOrElse(Validated.valid(()))

      def checkMax(x: Double): Schema.SchemaValidatedResult = max.map {
        case Inclusive(m) => check((x <= m) -> s"$num should be more or equal than $m")
        case Exclusive(m) => check((x < m) -> s"$num should be more than $m")
      } getOrElse(Validated.valid(()))

      checkMin(num.toDouble) *> checkMax(num.toDouble)
    }) getOrElse (Validated.valid(()))
  }

  private def check(pair: (Boolean, String)) = Validated.condNec(pair._1, (), pair._2)
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
