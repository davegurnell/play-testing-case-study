package databases

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class PasswordDatabase @Inject() (
  val dbConfigProvider: DatabaseConfigProvider
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  case class PasswordRow(id: Long, password: String)

  class PasswordTable(tag: Tag) extends Table[PasswordRow](tag, "passwords") {
    val accountId = column[Long]("account_id", O.PrimaryKey)
    val password  = column[String]("password")
    def * = (accountId, password).mapTo[PasswordRow]
  }

  val PasswordTable = TableQuery[PasswordTable]

  def checkPassword(accountId: Long, password: String): DBIO[Boolean] = {
    PasswordTable
      .filter(_.accountId === accountId)
      .map(_.password)
      .result
      .headOption
      .map(_.fold(false)(_ == password))
  }

  def setPassword(accountId: Long, password: String): DBIO[Unit] = {
    PasswordTable
      .filter(_.accountId === accountId)
      .insertOrUpdate(PasswordRow(accountId, password))
      .map(_ => ())
  }

  def delete(accountId: Long): DBIO[Unit] = {
    PasswordTable
      .filter(_.accountId === accountId)
      .delete
      .map(_ => ())
  }
}