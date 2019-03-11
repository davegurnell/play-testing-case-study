package services

import databases.{AccountDatabase, PasswordDatabase}
import javax.inject._
import models.Account
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.dbio.DBIO
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AccountService @Inject()(
  val dbConfigProvider: DatabaseConfigProvider,
  val accountDatabase: AccountDatabase,
  val passwordDatabase: PasswordDatabase,
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def open(password: String, balance: Double): Future[Account] = {
    db.run {
      for {
        acct <- accountDatabase.create(balance)
        _    <- passwordDatabase.setPassword(acct.id, password)
      } yield acct
    }
  }

  def read(accountId: Long): Future[Account] = {
    db.run {
      accountDatabase.read(accountId)
    }
  }

  def transfer(fromId: Long, toId: Long, amount: Double): Future[Account] = {
    db.run {
      for {
        from <- accountDatabase.read(fromId)
        to   <- accountDatabase.read(toId)
        _    <- accountDatabase.update(fromId, from.balance - amount)
        _    <- accountDatabase.update(toId,   to.balance   + amount)
        acct <- accountDatabase.read(fromId)
      } yield acct
    }
  }

  def applyInterest(multiplier: Double): Future[Unit] = {
    db.run {
      accountDatabase.list.flatMap { accounts =>
        DBIO.sequence {
          accounts.map { account =>
            accountDatabase.update(account.id, account.balance * multiplier)
          }
        }.map(_ => ())
      }
    }
  }
}
