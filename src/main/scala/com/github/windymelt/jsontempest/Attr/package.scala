package com.github.windymelt.jsontempest

import io.circe.Json

package object Attr {
  trait Attr {
    def validateThis(json: Json): Schema.SchemaValidatedResult
  }
  trait AttrObject {
    def fromSchema(s: Schema): Option[Attr]
  }
}
