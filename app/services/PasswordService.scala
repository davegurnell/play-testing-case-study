package services

import databases.PasswordDatabase
import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PasswordService @Inject()(
  val dbConfigProvider: DatabaseConfigProvider,
  val passwordDatabase: PasswordDatabase,
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def checkPassword(accountId: Long, password: String): Future[Boolean] = {
    db.run(passwordDatabase.checkPassword(accountId, password))
  }

  def setPassword(accountId: Long, password: String): Future[Unit] = {
    db.run(passwordDatabase.setPassword(accountId, password))
  }
}
