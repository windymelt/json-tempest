package com.github.windymelt.jsontempest

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SchemaSpec extends AnyFlatSpec with Matchers {
  "Schema.fromString" should "parse fundamental schema" in {
    val schema = """
    {
      "$schema": "https://json-schema.org/draft/2020-12/schema",
      "$id": "https://example.com/foo.schema.json",
      "title": "foo",
      "description": "foo",
      "type": "object",
      "properties": {
        "foo": {
          "description": "foo is int",
          "type": "integer"
        }
      }
    }
    """
    val Right(parsedSchema) = Schema.fromString(schema)
    parsedSchema.title shouldBe Some("foo")
    parsedSchema.description shouldBe Some("foo")
    parsedSchema.`type` shouldBe "object"
  }
}
