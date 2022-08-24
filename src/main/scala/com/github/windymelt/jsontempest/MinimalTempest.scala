package com.github.windymelt.jsontempest

trait MinimalTempest {
  case class Foo(foo: Int)
  case class Schema()

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
    val parsedSchema = Schema()
    val parsedJson = decode[Foo](json)
    parsedJson.map { validateJson(parsedSchema, _) } getOrElse (false)
  }

  private def validateJson(schema: Schema, json: Foo): Boolean = {
    true
  }
}
