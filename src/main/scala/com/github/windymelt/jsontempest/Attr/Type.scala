package com.github.windymelt.jsontempest.Attr

import io.circe.Json
import io.circe.Json.JArray
import io.circe.Json.JBoolean
import io.circe.Json.JNumber
import io.circe.Json.JNull
import io.circe.Json.JObject
import io.circe.Json.JString

import shapeless._

import com.github.windymelt.jsontempest.Attr.Attr
import com.github.windymelt.jsontempest.Schema
import cats.data.{Validated, ValidatedNec}

final case class Type(`type`: String :+: Set[String] :+: CNil) extends Attr {
  def validateThis(json: Json) = json match {
    case j if j.isArray   => checkType("array")
    case j if j.isBoolean => checkType("boolean")
    case j if j.isNumber =>
      val js = j.toString
      js contains(".") match {
	case true =>
          js endsWith(".0") match {
	    case true => checkType("number") orElse checkType("integer")
            case false => checkType("number") orElse checkType("float")
          }
        case false => checkType("number") orElse checkType("integer")
      }
    case j if j.isNull   => checkType("null")
    case j if j.isObject => checkType("object")
    case j if j.isString => checkType("string")
    case otherwise       => ???
  }

  private def checkType(expected: String): ValidatedNec[String, Unit] = {
    `type` match {
      case Inl(typeString) => Validated.condNec(expected == typeString, (), s"Type expected $typeString but received $expected")
      case Inr(tail) =>
        tail match {
          case Inl(typeStrings) =>
            lazy val typeStringsConcat = typeStrings.mkString("|")
            Validated.condNec(typeStrings contains expected, (), s"Type expected ${typeStringsConcat} but received $expected}")
          case Inr(_)           => ???
        }
    }
  }
}

object Type extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.`type`.map(Type.apply)
}
