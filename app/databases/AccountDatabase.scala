package databases

import javax.inject._
import models.Account
import play.api.db.slick._
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class AccountDatabase @Inject() (val dbConfig: DatabaseConfig[JdbcProfile])(implicit ec: ExecutionContext) {
  import dbConfig.profile.api._

  class AccountTable(tag: Tag) extends Table[Account](tag, "accounts") {
    val id      = column[Long]("id", O.PrimaryKey, O.AutoInc)
    val balance = column[Double]("balance")
    def * = (id, balance).mapTo[Account]
  }

  val AccountTable = TableQuery[AccountTable]

  def list: DBIO[Seq[Account]] = {
    AccountTable.result
  }

  def read(id: Long): DBIO[Account] = {
    AccountTable
      .filter(_.id === id)
      .result
      .head
  }

  def create(balance: Double): DBIO[Account] = {
    AccountTable
      .returning(AccountTable.map(_.id))
      .into((acct, id) => acct.copy(id = id))
      .+=(Account(-1L, balance))
  }

  def update(accountId: Long, balance: Double): DBIO[Account] = {
    val acct = Account(accountId, balance)

    AccountTable
      .filter(_.id === acct.id)
      .update(acct)
      .map(_ => acct)
  }

  def delete(id: Long): DBIO[Unit] = {
    AccountTable
      .filter(_.id === id)
      .delete
      .map(_ => ())
  }
}
