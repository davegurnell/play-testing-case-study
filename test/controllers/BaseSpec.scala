package controllers

import org.scalatest._
import org.scalatestplus.play.guice._
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test._

trait BaseSpec extends WordSpecLike with Matchers with GuiceOneServerPerSuite with FutureAwaits with DefaultAwaitTimeout {
  protected def appBuilder: GuiceApplicationBuilder =
    new GuiceApplicationBuilder()
      .configure(config)

  def config: Map[String, String] =
    Map(
      "slick.dbs.default.db.profile" -> "slick.jdbc.H2Profile$",
      "slick.dbs.default.db.driver" -> "org.h2.Driver",
      "slick.dbs.default.db.url" -> "jdbc:h2:mem:play;DB_CLOSE_DELAY=-1",
      "play.evolutions.enabled" -> "true",
      "play.evolutions.autoApply" -> "true",
      "play.evolutions.autoApplyDowns" -> "true",
    )

 override implicit lazy val app: Application =
   appBuilder.build()
}
