package com.github.windymelt.jsontempest

import com.github.windymelt.jsontempest.Attr.Attr

trait MinimalTempest {
  case class Foo(foo: Int)
  case class Schema(
      `$schema`: String,
      `$id`: String,
      title: String,
      description: String,
      attrs: Seq[Attr]
  )

  /** Very primitive stub validate function.
    *
    * @return
    */
  def validate(schema: String)(json: String): Boolean = {
    import io.circe._
    import io.circe.generic.auto._
    import io.circe.parser._
    import io.circe.syntax._

    // parse json
    // foo should be integer
    val parsedSchema = Schema(
      "https://json-schema.org/draft/2020-12/schema",
      "https://example.com/foo.schema.json",
      "foo",
      "foo",
      Seq(Attr.Type("object"))
    )
    val parsedJson = decode[Foo](json)
    parsedJson.map { validateJson(parsedSchema, _) } getOrElse (false)
  }

  private def validateJson(schema: Schema, json: Foo): Boolean = {
    true
  }
}
