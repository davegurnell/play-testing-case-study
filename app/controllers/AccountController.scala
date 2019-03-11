package controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import services.AccountService

import scala.concurrent.ExecutionContext

case class OpenAccountRequest(password: String, balance: Double)

case class TransferRequest(fromId: Long, toId: Long, amount: Double)

case class InterestRequest(multiplier: Double)

@Singleton
class AccountController @Inject()(
  val cc: ControllerComponents,
  val accountService: AccountService,
)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  implicit val openAccountRequestReads: Reads[OpenAccountRequest] =
    Json.reads

  implicit val transferRequest: Reads[TransferRequest] =
    Json.reads

  implicit val interestRequest: Reads[InterestRequest] =
    Json.reads

  def open: Action[OpenAccountRequest] = {
    Action.async(parse.json[OpenAccountRequest]) { request =>
      val OpenAccountRequest(password, balance) =
        request.body

      accountService.open(password, balance)
        .map(account => Ok(Json.toJson(account)))
    }
  }

  def read(accountId: Long): Action[AnyContent] = {
    Action.async {
      accountService.read(accountId)
        .map(account => Ok(Json.toJson(account)))
    }
  }

  def transfer: Action[TransferRequest] = {
    Action.async(parse.json[TransferRequest]) { request =>
      val TransferRequest(fromId, toId, amount) =
        request.body

      accountService.transfer(fromId, toId, amount).flatMap { _ =>
        accountService.read(fromId)
          .map(account => Ok(Json.toJson(account)))
      }
    }
  }

  def applyInterest: Action[InterestRequest] = {
    Action.async(parse.json[InterestRequest]) { request =>
      val InterestRequest(multiplier) =
        request.body

      accountService.applyInterest(multiplier)
        .map(_ => Ok(Json.obj()))
    }
  }
}
