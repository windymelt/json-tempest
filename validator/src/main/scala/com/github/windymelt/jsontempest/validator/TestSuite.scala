package com.github.windymelt.jsontempest
package validator

import io.circe.generic.semiauto._, io.circe.Decoder
import com.github.windymelt.jsontempest.Schema
import com.github.windymelt.jsontempest.Schema.SchemaDecoder

final case class TestSuite(
  description: String,
  schema: Schema,
  tests: Seq[Test],
)
