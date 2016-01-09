package com.github.selkhlifi.ddd.bank.services


import com.github.selkhlifi.ddd.bank.domain.BankAccount
import com.github.selkhlifi.ddd.bank.domain.money.Money
import com.github.selkhlifi.ddd.bank.repositories.BankAccountRepository

class BankingService {

  private val ACCOUNTNUMBER_FORMAT_REGEXP = """[0-9]{3}\.[0-9]{3}""".r

  def withdraw(bankAccountNumber: String, money: Money) = {
    val account = getAccount(bankAccountNumber)
    try {
      account.withdraw(money)
    } catch {
      case _ : AssertionError => throw new BankAccountOverdraft(s"Bank account: $bankAccountNumber, amount: $money")
      case ex : IllegalArgumentException => throw ex
      case ex : Throwable => throw new Error(s"Failed to register new bank account.$ex")
    }
  }

  def deposit(bankAccountNumber: String, money: Money) = {
    val account = getAccount(bankAccountNumber)
    account.deposit(money)
  }

  def balance(bankAccountNumber: String): Money = {
    val account = getAccount(bankAccountNumber)
    account.balance
  }

  def registerBankAccount(bankAccount: BankAccount) = {
    validateBankAccountNumberFormat(bankAccount)
    BankAccountRepository.create(bankAccount)
  }

  private def getAccount(bankAccountNumber: String) : BankAccount = {
    val accountOption = BankAccountRepository.findBankAccountWithAccountNumber(bankAccountNumber)
    accountOption.getOrElse(throw new BankAccountNotFound)
  }

  private def validateBankAccountNumberFormat(bankAccount: BankAccount): Unit = {
    bankAccount.accountNumber match {
      case ACCOUNTNUMBER_FORMAT_REGEXP() => /* good account number */
      case _ => throw new IllegalArgumentException("Failed to register" +
        "new bank account number format: " + bankAccount.accountNumber)
    }
  }
}
