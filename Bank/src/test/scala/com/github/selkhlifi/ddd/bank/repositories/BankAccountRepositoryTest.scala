package com.github.selkhlifi.ddd.bank.repositories

import com.github.selkhlifi.ddd.bank.domain.BankAccount
import com.github.selkhlifi.ddd.bank.services.BankAccountAlreadyExists
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BankAccountRepositoryTest extends FunSuite with BeforeAndAfterEach {
  override protected def beforeEach() {
    BankAccountRepository.clear()
  }


  val NEW_BANK_ACCOUNTNUMBER: String = "0000-0001"

  test("It should be possible to create a new bank account using an" +
  " account number that is not assigned to an existing bank account") {
    val bankAccount: BankAccount = new BankAccount
    bankAccount.accountNumber = NEW_BANK_ACCOUNTNUMBER
    BankAccountRepository.create(bankAccount)
  }

  test("It should not be possible to create a bank account using an" +
    " account number for which a bank account has already been created") {
    val bankAccount1: BankAccount = new BankAccount
    bankAccount1.accountNumber = NEW_BANK_ACCOUNTNUMBER
    BankAccountRepository.create(bankAccount1)

    val bankAccount2: BankAccount = new BankAccount
    bankAccount2.accountNumber = NEW_BANK_ACCOUNTNUMBER
    intercept[AssertionError] {
      BankAccountRepository.create(bankAccount2)
    }
  }

  test("It should be possible to retrieve a bank account that has been" +
    " created earlier using its account number") {
    val bankAccount: BankAccount = new BankAccount
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
    val bankAccount = new BankAccount
    bankAccount.accountNumber = NEW_BANK_ACCOUNTNUMBER
    BankAccountRepository.create(bankAccount)

    bankAccount.balance = 100.0
    BankAccountRepository.update(bankAccount)

    val theReadBankAccountOption = BankAccountRepository
      .findBankAccountWithAccountNumber(NEW_BANK_ACCOUNTNUMBER)
    assert(theReadBankAccountOption.isDefined)
    assert(theReadBankAccountOption.get.accountNumber == NEW_BANK_ACCOUNTNUMBER)
    assert(theReadBankAccountOption.get.balance == 100.0)
  }
  test("It should not be possible to update a bank account that has not" +
    " been created earlier") {
    val theBankAccount = new BankAccount()
    theBankAccount.accountNumber = NEW_BANK_ACCOUNTNUMBER
    intercept[AssertionError] {
      BankAccountRepository.update(theBankAccount)
    }
  }
}
