package com.github.selkhlifi.ddd.bank.domain

import java.util.Currency

import com.github.selkhlifi.ddd.bank.domain.money.Money
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, FunSuite}

@RunWith(classOf[JUnitRunner])
class BankAccountTest extends FunSuite with BeforeAndAfterEach {
  val CURRENCY: Currency = Currency.getInstance("MAD")

  override def beforeEach(): Unit = super.beforeEach()

  test("A new bank account should have a zero balance") {
    assert(new BankAccount(CURRENCY).balance equals Money(0.0, CURRENCY))
  }

  test("When money is deposited to a bank account, " +
    "the balance should increase accordingly") {
    val account = new BankAccount(CURRENCY)
    account.deposit(Money(10, CURRENCY))
    assert(account.balance equals Money(10, CURRENCY))
  }

  test("It should not be possible to deposit a negative amount of money" +
    " to a bank account") {
    val account = new BankAccount(CURRENCY)
    intercept[IllegalArgumentException] {
      account.deposit(Money(-10.0, CURRENCY))
      /* Should not arrive here. */
      assert(false)
    }
    val theBalance = account.balance
    assert(theBalance equals Money(0.0, CURRENCY))
  }

  test("When money is withdrawn from a bank account, " +
    "the balance should decrease accordingly") {
    val account = new BankAccount(CURRENCY)
    account.deposit(Money(100, CURRENCY))

    account.withdraw(Money(50, CURRENCY))

    assert(account.balance equals Money(50, CURRENCY))
  }

  test("It should not be possible to withdraw a negative amount of money" +
    " from a bank account") {
    val account = new BankAccount(CURRENCY)
    account.deposit(Money(100, CURRENCY))
    intercept[IllegalArgumentException] {
      account.withdraw(Money(-50, CURRENCY))
      assert(false)
    }
    assert(account.balance == Money(100, CURRENCY))
  }

  test("It should not be possible to overdraft a bank account") {
    val account = new BankAccount(CURRENCY)
    account.deposit(Money(50, CURRENCY))
    intercept[AssertionError] {
      account.withdraw(Money(100, CURRENCY))
    }
    assert(account.balance.amount == 50)
  }

  test("It should be possible to clone a bank account") {
    val accountNumber = "123-456"
    val balance = 345.67
    val bankAccount = new BankAccount(CURRENCY)

    bankAccount.accountNumber = accountNumber
    bankAccount.balance = Money(balance, CURRENCY)

    val clone: BankAccount= bankAccount.clone

    assert(clone ne bankAccount)
    assert(clone.balance == bankAccount.balance)
    assert(clone.accountNumber == bankAccount.accountNumber)
  }
}
