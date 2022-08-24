package com.github.windymelt.jsontempest

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class MinimalTempestSpec extends AnyFlatSpec with Matchers with MinimalTempest {
  "Tempest" should "validate fundamental json" in {
    validate("""{"foo": 12345}""") shouldEqual true
  }
}
