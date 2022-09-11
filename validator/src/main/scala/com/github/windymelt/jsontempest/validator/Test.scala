package com.github.windymelt.jsontempest.validator

import io.circe.generic.semiauto._, io.circe.Decoder, io.circe.Json
final case class Test(description: String, data: Json, valid: Boolean)
