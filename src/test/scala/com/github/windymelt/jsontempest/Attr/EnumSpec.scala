package com.github.windymelt.jsontempest
package Attr

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.circe._
import io.circe.parser._

class EnumSpec extends AnyFlatSpec with Matchers {
  "Attr.Enum" should "treat Enum" in {
    val schema = """
    {
      "$schema": "https://json-schema.org/draft/2020-12/schema",
      "$id": "https://example.com/foo.schema.json",
      "title": "foo",
      "description": "foo",
      "type": "object",
      "properties": {
        "foo": {
          "description": "foo is f | b | z",
          "type": "string",
          "enum": ["f", "b", "z"]
        }
      }
    }
    """
    val Right(parsedSchema) = Schema.fromString(schema)
    val Right(json1) = parse("""{"foo": "f"}""")
    val Right(json2) = parse("""{"foo": "b"}""")
    val Right(json3) = parse("""{"foo": "z"}""")
    val Right(json4) = parse("""{"foo": "q"}""")
    val Right(json5) = parse("""{"foo": 42}""")
    parsedSchema.validate(json1) shouldBe true
    parsedSchema.validate(json2) shouldBe true
    parsedSchema.validate(json3) shouldBe true
    parsedSchema.validate(json4) shouldBe false
    parsedSchema.validate(json5) shouldBe false
  }

  it should "treat heterogeneous Enum" in {
    val schema = """
    {
      "$schema": "https://json-schema.org/draft/2020-12/schema",
      "$id": "https://example.com/foo.schema.json",
      "title": "foo",
      "description": "foo",
      "type": "object",
      "properties": {
        "foo": {
          "description": "foo is foo | bar | 123",
          "type": ["string", "number"],
          "enum": ["foo", "bar", 123]
        }
      }
    }
    """
    val Right(parsedSchema) = Schema.fromString(schema)
    val Right(json1) = parse("""{"foo": "foo"}""")
    val Right(json2) = parse("""{"foo": "bar"}""")
    val Right(json3) = parse("""{"foo": 123}""")
    val Right(json4) = parse("""{"foo": "q"}""")
    val Right(json5) = parse("""{"foo": 42}""")
    parsedSchema.validate(json1) shouldBe true
    parsedSchema.validate(json2) shouldBe true
    parsedSchema.validate(json3) shouldBe true
    parsedSchema.validate(json4) shouldBe false
    parsedSchema.validate(json5) shouldBe false
  }
}
