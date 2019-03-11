package controllers

import akka.stream.Materializer
import models.Account
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test._
import services.{AccountService, PasswordService}

import scala.concurrent.{ExecutionContext, Future}

class AccountControllerSpec extends BaseSpec {
  implicit lazy val executionContext: ExecutionContext =
    app.injector.instanceOf[ExecutionContext]

  implicit lazy val materializer: Materializer =
    app.injector.instanceOf[Materializer]

  lazy val accountService: AccountService =
    app.injector.instanceOf[AccountService]

  lazy val passwordService: PasswordService =
    app.injector.instanceOf[PasswordService]

  lazy val accountController: AccountController =
    app.injector.instanceOf[AccountController]

  "open endpoint" should {
    "create an account with the specified balance and password" in {
      val result: Future[Result] =
        accountController.open().apply {
          FakeRequest(routes.AccountController.open())
            .withBody(OpenAccountRequest("passw0rd", 100.0))
        }

      println(contentAsString(result))

      status(result) should be(OK)
      contentType(result) should be(Some("application/json"))

      val account: Account =
        contentAsJson(result).as[Account]

      // The endpoint should return a sensible result:
      account.id should be >= 0L
      account.balance should be(100.0)

      // The account balance and password should be stored appropriately:
      await(accountService.read(account.id)) shouldBe account
      await(passwordService.checkPassword(account.id, "passw0rd")) should be(true)
    }
  }
}
