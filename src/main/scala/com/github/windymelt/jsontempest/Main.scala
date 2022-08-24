package com.github.windymelt.jsontempest

object Main extends MinimalTempest with App {
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
  val json = """{"foo": 12345}"""
  validate(schema)(json) match {
    case true  => sys.exit(0) // valid
    case false => sys.exit(1) // invalid
  }
}
