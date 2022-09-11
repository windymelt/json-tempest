package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr.AttrObject
import com.github.windymelt.jsontempest.Schema
import io.circe.Json
import cats.data.{Chain, Validated, ValidatedNec}
import Validated._
import cats.implicits._
import cats.syntax._

final case class Length(inclusiveMin: Option[Int], inclusiveMax: Option[Int]) extends Attr {
  def validateThis(json: Json) = {
    def checkMin(x: Int): ValidatedNec[String, Unit] =
      inclusiveMin.map(min => Validated.condNec(min <= x, (), "Violation of minimum length")).getOrElse(valid(()))
    def checkMax(x: Int): ValidatedNec[String, Unit] =
      inclusiveMax.map(max => Validated.condNec(x <= max, (), "Violation of maximum length")).getOrElse(valid(()))

    val validated = json.asString match {
      case Some(str) =>
        val cpcount = str.codePointCount(0, str.length)
        checkMin(cpcount) *> checkMax(cpcount)
      case None => valid(())
    }

    validated
  }
}

final object MinLength extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] =
    s.minLength.map(x => Length(Some(x), None))
}
final object MaxLength extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] =
    s.maxLength.map(x => Length(None, Some(x)))
}
