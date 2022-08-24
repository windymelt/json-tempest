package com.github.windymelt.jsontempest

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MinimalTempestSpec extends AnyFlatSpec with Matchers with MinimalTempest {
  "Tempest" should "validate fundamental json" in {
    val jsonInt = """{"foo": 12345}"""
    val jsonString = """{"foo": "bar"}"""
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
    validate(schema)(jsonInt) shouldEqual true
    validate(schema)(jsonString) shouldEqual false

    val schema2 = """
    {
      "$schema": "https://json-schema.org/draft/2020-12/schema",
      "$id": "https://example.com/foo.schema.json",
      "title": "foo",
      "description": "foo",
      "type": "object",
      "properties": {
        "foo": {
          "description": "foo is negative int",
          "type": "integer",
          "exclusiveMaximum": 0
        }
      }
    }
    """
    validate(schema2)(jsonInt) shouldEqual false
    val jsonNegativeInt = """{"foo": -12345}"""
    validate(schema2)(jsonNegativeInt) shouldEqual true
  }
}
