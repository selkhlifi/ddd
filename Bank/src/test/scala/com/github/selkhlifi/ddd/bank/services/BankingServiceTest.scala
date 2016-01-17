package com.github.selkhlifi.ddd.bank.services

import java.util.Currency

import com.github.selkhlifi.ddd.bank.domain.BankAccount
import com.github.selkhlifi.ddd.bank.domain.money.Money
import com.github.selkhlifi.ddd.bank.repositories.BankAccountRepository
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BankingServiceTest extends FunSuite with BeforeAndAfterEach {
  private val CURRENCY = Currency.getInstance("MAD")
  val BANK_ACCOUNT_NUMBER = "123.123"
  val BANK_ACCOUNT_NUMBER_BAD_FORMAT = "123-123"

  var bankingService: BankingService = _
  var newBankAccount: BankAccount = _

  override protected def beforeEach(): Unit ={
    bankingService = new BankingService
    BankAccountRepository.clear()
    newBankAccount = new BankAccount(CURRENCY)
    newBankAccount.accountNumber = BANK_ACCOUNT_NUMBER
  }

  test("It should be possible to create a new bank account with " +
    "an account number that has not previously been used") {
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
    newBankAccount.balance = Money(100.3, CURRENCY)
    bankingService.registerBankAccount(newBankAccount)
    assert(bankingService.balance(BANK_ACCOUNT_NUMBER).equals(Money(100.3, CURRENCY)))
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
    bankingService.deposit(BANK_ACCOUNT_NUMBER, Money(100.3, CURRENCY))
    val theBalance = bankingService.balance(BANK_ACCOUNT_NUMBER)

    assert(theBalance.equals(Money(100.3, CURRENCY)))
  }
  test("It should not be possible to deposit money using an " +
    "account number for witch there is no bank account") {
    intercept[BankAccountNotFound] {
      bankingService.deposit(BANK_ACCOUNT_NUMBER, Money(100.0, CURRENCY))
    }
  }
  test("When money is withdrawn from bank account, the " +
    "account balance should decrease accordingly") {
    bankingService.registerBankAccount(newBankAccount)
    bankingService.deposit(BANK_ACCOUNT_NUMBER, Money(100.3, CURRENCY))
    bankingService.withdraw(BANK_ACCOUNT_NUMBER, Money(100.0, CURRENCY))
    assert(bankingService.balance(BANK_ACCOUNT_NUMBER).equals(Money(0.3, CURRENCY)))

  }
  test("It should not be possible to withdraw money using an " +
    "account number for witch there is no bank account") {
    intercept[BankAccountNotFound] {
      bankingService.withdraw(BANK_ACCOUNT_NUMBER, Money(10.3, CURRENCY))
    }
  }
  test("It should not be possible to overdraft a bank account") {
    bankingService.registerBankAccount(newBankAccount)
    bankingService.deposit(BANK_ACCOUNT_NUMBER, Money(100.3, CURRENCY))
    intercept[BankAccountOverdraft] {
      bankingService.withdraw(BANK_ACCOUNT_NUMBER, Money(200.4, CURRENCY))
    }
    assert(bankingService.balance(BANK_ACCOUNT_NUMBER).equals(Money(100.3, CURRENCY)))
  }
}
