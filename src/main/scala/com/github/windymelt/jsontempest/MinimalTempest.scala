package com.github.windymelt.jsontempest

trait MinimalTempest {

  /** Very primitive stub validate function.
    *
    * @return
    */
  def validate(schema: String)(json: String): Boolean = {
    true
  }
}
