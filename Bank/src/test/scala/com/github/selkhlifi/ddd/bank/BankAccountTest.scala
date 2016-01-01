package com.github.selkhlifi.ddd.bank

import com.github.selkhlifi.ddd.bank.domain.BankAccount
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, FunSuite}

@RunWith(classOf[JUnitRunner])
class BankAccountTest extends FunSuite with BeforeAndAfterEach {
  override def beforeEach(): Unit = super.beforeEach()

  test("A new bank account should have a zero balance") {
    assert(new BankAccount().balance == 0.0)
  }
  test("When money is deposited to a bank account, " +
    "the balance should increase accordingly") {
    val account = new BankAccount
    account.deposit(10)
    assert(account.balance == 10)
  }
  test("It should not be possible to deposit a negative amount of money" +
    " to a bank account") {
    val account = new BankAccount
    intercept[IllegalArgumentException] {
      account.deposit(-10.0)
      /* Should not arrive here. */
      assert(false)
    }
    val theBalance = account.balance
    assert(theBalance == 0.0)
  }
  test("When money is withdrawn from a bank account, " +
    "the balance should decrease accordingly") {
    val account = new BankAccount
    account.deposit(100)

    account.withdraw(50)

    assert(account.balance == 50)
  }
  test("It should not be possible to withdraw a negative amount of money" +
    " from a bank account") {
    val account = new BankAccount
    account.deposit(100)
    intercept[IllegalArgumentException] {
      account.withdraw(-50)
      assert(false)
    }
    assert(account.balance == 100)
  }
  test("It should not be possible to overdraft a bank account") {
    val account = new BankAccount
    account.deposit(50)
    intercept[AssertionError] {
      account.withdraw(100)
    }
    assert(account.balance == 50)
  }
  test("It should be possible to clone a bank account") {
    val accountNumber = "123-456"
    val balance = 345.67
    val bankAccount = new BankAccount

    bankAccount.accountNumber = accountNumber
    bankAccount.balance = balance

    val clone: BankAccount= bankAccount.clone

    assert(clone ne bankAccount)
    assert(clone.balance == bankAccount.balance)
    assert(clone.accountNumber == bankAccount.accountNumber)
  }

}
