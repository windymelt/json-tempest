package com.github.windymelt.jsontempest.Attr

import com.github.windymelt.jsontempest.Attr

final case class Type(`type`: String) extends Attr {
  def validateThis(something: Any): Boolean = true
}
