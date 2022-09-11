package com.github.windymelt.jsontempest.Attr

import io.circe.Json

import com.github.windymelt.jsontempest.Attr.Attr
import com.github.windymelt.jsontempest.Schema
import com.github.windymelt.jsontempest.MinimalTempest
import cats.data.Validated
import cats.implicits._

final case class Properties(props: Map[String, Schema]) extends Attr {
  def validateThis(json: Json): Schema.SchemaValidatedResult = {
    json.asObject match {
      case None => Validated.valid(())
      case Some(jo) =>
        val validatedProps = props.map { case (k, v) =>
          jo(k).map(foundProp => v.validate(foundProp))
        }
        if (validatedProps.find(_.isEmpty).isDefined) { return Validated.valid(()) }
        validatedProps.flatten.reduce(_ *> _)
    }
  }
}

object Properties extends AttrObject {
  def fromSchema(s: Schema): Option[Attr] = s.properties map { prop =>
    Properties(prop)
  }
}
