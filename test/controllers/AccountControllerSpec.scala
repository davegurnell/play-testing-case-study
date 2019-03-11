package controllers

import akka.stream.Materializer
import models.Account
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Application
import play.api.db.evolutions.Evolutions
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test._
import services.{AccountService, PasswordService}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class AccountControllerSpec extends PlaySpec with GuiceOneAppPerSuite with Injecting {
  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "slick.dbs.default.profile" -> "slick.jdbc.H2Profile$",
        "slick.dbs.default.db.driver" -> "org.h2.Driver",
        "slick.dbs.default.db.url" -> "jdbc:h2:mem:play",
        "play.evolutions.enabled" -> "true",
        "play.evolutions.autoApply" -> "true",
        "play.evolutions.autoApplyDowns" -> "true",
      )
      .build()

  trait Mocks {
    implicit def executionContext: ExecutionContext =
      app.injector.instanceOf[ExecutionContext]

    implicit def materializer: Materializer =
      app.injector.instanceOf[Materializer]

    def accountService: AccountService =
      app.injector.instanceOf[AccountService]

    def passwordService: PasswordService =
      app.injector.instanceOf[PasswordService]

    def accountController: AccountController =
      app.injector.instanceOf[AccountController]
  }

  "open endpoint" should {
    "create an account with the specified balance and password" in new Mocks {
      val result: Future[Result] =
        accountController.open().apply {
          FakeRequest(routes.AccountController.open())
            .withBody(OpenAccountRequest("passw0rd", 100.0))
        }

      println(contentAsString(result))

      status(result) mustBe OK
      contentType(result) mustBe Some("application/json")

      val account: Account =
        contentAsJson(result).as[Account]

      // The endpoint should return a sensible result:
      account.id must be >= 0L
      account.balance mustBe 100.0

      // The account balance and password should be stored appropriately:
      await(accountService.read(account.id)) mustBe account
      await(passwordService.checkPassword(account.id, "passw0rd")) mustBe true
    }
  }
}
