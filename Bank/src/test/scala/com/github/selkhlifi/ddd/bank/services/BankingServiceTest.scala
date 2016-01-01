package com.github.selkhlifi.ddd.bank.services

import com.github.selkhlifi.ddd.bank.domain.BankAccount
import com.github.selkhlifi.ddd.bank.repositories.BankAccountRepository
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BankingServiceTest extends FunSuite with BeforeAndAfterEach {
  val BANK_ACCOUNT_NUMBER = "123.123"
  val BANK_ACCOUNT_NUMBER_BAD_FORMAT = "123-123"

  var bankingService: BankingService = _
  var newBankAccount: BankAccount = _
  override protected def beforeEach(): Unit ={
    bankingService = new BankingService
    BankAccountRepository.clear()
    newBankAccount = new BankAccount
    newBankAccount.accountNumber = BANK_ACCOUNT_NUMBER
  }

  test("It should be possible to create a new bank account with " +
    "an account number that has not previously been used") {
    println(newBankAccount.accountNumber)
    bankingService.registerBankAccount(newBankAccount)
  }

  test("It should not be possible to create a bank account with " +
    "an account number that previously has been used") {
    bankingService.registerBankAccount(newBankAccount)
    intercept[BankAccountAlreadyExists] {
      bankingService.registerBankAccount(newBankAccount)
    }
  }
  test("It should not be possible to create a bank account with an " +
    "account number that is of illegal format") {
    newBankAccount.accountNumber = BANK_ACCOUNT_NUMBER_BAD_FORMAT
    intercept[IllegalArgumentException] {
      bankingService.registerBankAccount(newBankAccount)
    }
  }
  test("It should be possible to perform a balance inquiry on an existing" +
    " bank account") {
    newBankAccount.balance = 100.3
    bankingService.registerBankAccount(newBankAccount)
    assert(bankingService.balance(BANK_ACCOUNT_NUMBER) == 100.3)
  }
  test("It should not be possible to perform a balance inquiry on an non-existing" +
    " bank account") {
    intercept[BankAccountNotFound]{
      bankingService.balance(BANK_ACCOUNT_NUMBER)
    }
  }
  test("When money is deposited to a bank account, the account balance " +
    "should increase accordingly") {
    bankingService.registerBankAccount(newBankAccount)
    bankingService.deposit(BANK_ACCOUNT_NUMBER, 100.3)
    val theBalance = bankingService.balance(BANK_ACCOUNT_NUMBER)

    assert(theBalance == 100.3)
  }
  test("It should not be possible to deposit money using an " +
    "account number for witch there is no bank account") {
    intercept[BankAccountNotFound] {
      bankingService.deposit(BANK_ACCOUNT_NUMBER, 100.0)
    }
  }
  test("When money is withdrawn from bank account, the " +
    "account balance should decrease accordingly") {
    bankingService.registerBankAccount(newBankAccount)
    bankingService.deposit(BANK_ACCOUNT_NUMBER, 100.3)
    bankingService.withdraw(BANK_ACCOUNT_NUMBER, 100.0)
    assert(bankingService.balance(BANK_ACCOUNT_NUMBER) == 0.3)

  }
  test("It should not be possible to withdraw money using an " +
    "account number for witch there is no bank account") {
    intercept[BankAccountNotFound] {
      bankingService.withdraw(BANK_ACCOUNT_NUMBER, 10.3)
    }
  }
  test("It should not be possible to overdraft a bank account") {
    bankingService.registerBankAccount(newBankAccount)
    bankingService.deposit(BANK_ACCOUNT_NUMBER, 100.3)
    intercept[BankAccountOverdraft] {
      bankingService.withdraw(BANK_ACCOUNT_NUMBER, 200.4)
    }
    assert(bankingService.balance(BANK_ACCOUNT_NUMBER) == 100.3)
  }
}
