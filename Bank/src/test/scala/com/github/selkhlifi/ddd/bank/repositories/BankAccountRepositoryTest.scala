package com.github.selkhlifi.ddd.bank.repositories

import java.util.Currency

import com.github.selkhlifi.ddd.bank.domain.BankAccount
import com.github.selkhlifi.ddd.bank.domain.money.Money
import com.github.selkhlifi.ddd.bank.services.BankAccountAlreadyExists
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BankAccountRepositoryTest extends FunSuite with BeforeAndAfterEach {
  val CURRENCY: Currency = Currency.getInstance("MAD")

  override protected def beforeEach() {
    BankAccountRepository.clear()
  }


  val NEW_BANK_ACCOUNTNUMBER: String = "0000-0001"

  test("It should be possible to create a new bank account using an" +
  " account number that is not assigned to an existing bank account") {
    val bankAccount: BankAccount = new BankAccount(CURRENCY)
    bankAccount.accountNumber = NEW_BANK_ACCOUNTNUMBER
    BankAccountRepository.create(bankAccount)
  }

  test("It should not be possible to create a bank account using an" +
    " account number for which a bank account has already been created") {
    val bankAccount1: BankAccount = new BankAccount(CURRENCY)
    bankAccount1.accountNumber = NEW_BANK_ACCOUNTNUMBER
    BankAccountRepository.create(bankAccount1)

    val bankAccount2: BankAccount = new BankAccount(CURRENCY)
    bankAccount2.accountNumber = NEW_BANK_ACCOUNTNUMBER
    intercept[AssertionError] {
      BankAccountRepository.create(bankAccount2)
    }
  }

  test("It should be possible to retrieve a bank account that has been" +
    " created earlier using its account number") {
    val bankAccount: BankAccount = new BankAccount(CURRENCY)
    bankAccount.accountNumber = NEW_BANK_ACCOUNTNUMBER
    BankAccountRepository.create(bankAccount)

    val theReadBankAccountOption =
      BankAccountRepository
        .findBankAccountWithAccountNumber(NEW_BANK_ACCOUNTNUMBER)
    assert(theReadBankAccountOption.isDefined)
    assert(NEW_BANK_ACCOUNTNUMBER
      equals(theReadBankAccountOption.get.accountNumber))
  }
  test("It should not be possible to retrieve a bank account using an" +
    " account number for which no bank account has been created") {
    val theReadBankAccountOption = BankAccountRepository
      .findBankAccountWithAccountNumber(NEW_BANK_ACCOUNTNUMBER)
    assert(theReadBankAccountOption.isEmpty)
  }
  test("It should be possible to update a bank account that has been" +
    " created earlier") {
    val bankAccount = new BankAccount(CURRENCY)
    bankAccount.accountNumber = NEW_BANK_ACCOUNTNUMBER
    BankAccountRepository.create(bankAccount)

    bankAccount.balance = Money(100.0, CURRENCY)
    BankAccountRepository.update(bankAccount)

    val theReadBankAccountOption = BankAccountRepository
      .findBankAccountWithAccountNumber(NEW_BANK_ACCOUNTNUMBER)
    assert(theReadBankAccountOption.isDefined)
    assert(theReadBankAccountOption.get.accountNumber == NEW_BANK_ACCOUNTNUMBER)
    assert(theReadBankAccountOption.get.balance.equals(Money(100.0, CURRENCY)))
  }
  test("It should not be possible to update a bank account that has not" +
    " been created earlier") {
    val theBankAccount = new BankAccount(CURRENCY)
    theBankAccount.accountNumber = NEW_BANK_ACCOUNTNUMBER
    intercept[AssertionError] {
      BankAccountRepository.update(theBankAccount)
    }
  }
}
