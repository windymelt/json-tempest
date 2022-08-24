package com.github.windymelt.jsontempest

object Hello extends MinimalTempest with App {
  val json = """{"foo": 12345}"""
  validate(json) match {
    case true  => sys.exit(0) // valid
    case false => sys.exit(1) // invalid
  }
}

trait MinimalTempest {

  /** Very primitive stub validate function.
    *
    * @return
    */
  def validate(json: String): Boolean = {
    true
  }
}
