package com.github.windymelt.jsontempest

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MinimalTempestSpec extends AnyFlatSpec with Matchers with MinimalTempest {
  "Tempest" should "validate fundamental json" in {
    val json = """{"foo": 12345}"""
    val schema = """
    {
      "$schema": "https://json-schema.org/draft/2020-12/schema",
      "$id": "https://example.com/product.schema.json",
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
    val badjson = """{"foo": "bar"}"""
    validate(schema)(json) shouldEqual true
    validate(schema)(badjson) shouldEqual false
  }
}
