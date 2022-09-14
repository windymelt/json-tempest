package com.github.windymelt.jsontempest.Attr

import io.circe.Json

import com.github.windymelt.jsontempest.Attr.Attr
import com.github.windymelt.jsontempest.Schema
import com.github.windymelt.jsontempest.MinimalTempest
import cats.data.Validated
import cats.implicits._

final case class Properties(props: Map[String, Schema], required: Option[Set[String]]) extends Attr {
  def validateThis(json: Json): Schema.SchemaValidatedResult = {
    json.asObject match {
      case None => Validated.valid(())
      case Some(jo) =>
        val validatedProps = props.map { case (k, v) =>
          jo(k).map(foundProp => v.validate(foundProp))
        }
        if (validatedProps.find(_.isEmpty).isDefined) { return Validated.valid(()) }
        validatedProps.flatten.reduce(_ *> _)
    }
  }
}

final case class BooleanProperties(props: Map[String, Boolean], required: Option[Set[String]] = None) extends Attr {
  import cats.implicits._

  def validateThis(json: Json) = {
    json.asObject match {
      case None => Validated.valid(())
      case Some(jo) =>
        val trueFalseMap: Map[Boolean, List[String]] = props.groupMapReduce(_._2){ case k -> v => List(k) }(_ |+| _)

        val caseTrue = (for {
          key <- trueFalseMap.get(true).toList.flatten
        } yield {
          // Special case "boolean property is true but not in required attribute"
          val keyRequired = required match {
	    case None => true
	    case Some(req) => req.contains(key) match {
	      case true => req(key)
              case false => false
            }
          }
          Validated.condNec(jo(key).isDefined || !keyRequired, (), s"$key should be defined")
        }).foldLeft(Validated.validNec[String, Unit](()))(_ *> _)

        val caseFalse = (for {
          key <- trueFalseMap.get(false).toList.flatten
        } yield {
          Validated.condNec(jo(key).isEmpty, (), s"$key should not be defined")
        }).foldLeft(Validated.validNec[String, Unit](()))(_ *> _)

        caseTrue *> caseFalse
    }
  }
}

object Properties extends AttrObject {
  import shapeless._
  def fromSchema(s: Schema): Option[Attr] = s.properties map {
    case Inl(props) => Properties(props, s.required)
    case Inr(Inl(booleanProps)) => BooleanProperties(booleanProps, s.required)
    case Inr(Inr(_)) => ???
  }
}
