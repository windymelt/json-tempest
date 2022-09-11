package com.github.windymelt.jsontempest
package Attr

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.circe._
import io.circe.parser._

class NotSpec extends AnyFlatSpec with Matchers {
  "Attr.Not" should "reverse validate result" in {
    val schema = """
    {
      "$schema": "https://json-schema.org/draft/2020-12/schema",
      "$id": "https://example.com/foo.schema.json",
      "title": "foo",
      "description": "foo",
      "type": "object",
      "properties": {
        "foo": {
          "not" : {"type": "string"}
        }
      }
    }
    """
    val Right(parsedSchema) = Schema.fromString(schema)
    val Right(json1) = parse("""{"foo": "bar"}""")
    val Right(json2) = parse("""{"foo": 123}""")
    parsedSchema.validate(json1) shouldBe false
    parsedSchema.validate(json2) shouldBe true
  }
}
