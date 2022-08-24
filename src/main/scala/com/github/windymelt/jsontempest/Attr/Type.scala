package com.github.windymelt.jsontempest.Attr

import io.circe.Json
import io.circe.Json.JArray
import io.circe.Json.JBoolean
import io.circe.Json.JNumber
import io.circe.Json.JNull
import io.circe.Json.JObject
import io.circe.Json.JString

import com.github.windymelt.jsontempest.Attr.Attr
import com.github.windymelt.jsontempest.Schema

final case class Type(`type`: String) extends Attr {
  def validateThis(json: Json): Boolean = json match {
    case j if j.isArray   => `type` == "array"
    case j if j.isBoolean => `type` == "boolean"
    case j if j.isNumber  => `type` == "integer"
    case j if j.isNull    => `type` == "null"
    case j if j.isObject  => `type` == "object"
    case j if j.isString  => `type` == "string"
    case otherwise        => ???
  }
}

object Type extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = Some(Type(s.`type`))
}
