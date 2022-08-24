package com.github.windymelt.jsontempest.Attr

import io.circe.Json

import com.github.windymelt.jsontempest.Attr.Attr
import com.github.windymelt.jsontempest.Schema
import com.github.windymelt.jsontempest.MinimalTempest

final case class Properties(props: Map[String, Schema]) extends Attr {
  def validateThis(json: Json): Boolean = {
    json.asObject match {
      case None => false
      case Some(jo) =>
        val validatedProps = props.map { case (k, v) =>
          jo(k).map(foundProp => v.validate(foundProp))
        }
        if (validatedProps.find(_.isEmpty).isDefined) { return false }
        validatedProps.flatten.reduce(_ && _)
    }
  }
}

object Properties extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.properties map { prop =>
    Properties(prop)
  }
}
