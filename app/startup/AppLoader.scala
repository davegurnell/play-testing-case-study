package startup

import controllers._
import databases._
import play.api.db.slick.{DbName, DefaultSlickApi}
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext}
import router.Routes
import services._
import slick.jdbc.JdbcProfile

class AppLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = {
    println("STARTING")
    val module = new StartupModule(context)
    module.application
  }
}

class StartupModule(context: ApplicationLoader.Context) extends BuiltInComponentsFromContext(context) {
  val httpFilters: Seq[EssentialFilter] =
    Seq.empty

  lazy val slickApi = new DefaultSlickApi(environment, configuration, applicationLifecycle)
  lazy val dbConfig = slickApi.dbConfig[JdbcProfile](DbName("default"))
  lazy val accountDatabase = new AccountDatabase(dbConfig)
  lazy val passwordDatabase = new PasswordDatabase(dbConfig)
  lazy val accountService = new AccountService(dbConfig, accountDatabase, passwordDatabase)
  lazy val accountController = new AccountController(controllerComponents, accountService)

  val router: Router = new Routes(
    httpErrorHandler,
    accountController,
    prefix = ""
  )
}
