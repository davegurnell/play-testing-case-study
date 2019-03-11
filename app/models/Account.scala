package models

import play.api.libs.json._

case class Account(id: Long, balance: Double)

object Account {
  def tupled: ((Long, Double)) => Account =
    (apply _).tupled

  implicit val format: OFormat[Account] =
    Json.format
}