package com.github.selkhlifi.ddd.bank.repositories

import com.github.selkhlifi.ddd.bank.domain.BankAccount
import com.github.selkhlifi.ddd.bank.services.{BankAccountNotFound, BankAccountAlreadyExists}

object BankAccountRepository {
  var accounts : List[BankAccount] = List()

  def update(bankAccount: BankAccount) = {
    val index: Int = accounts.indexOf(bankAccount)
    accounts = accounts updated (index, bankAccount)
  }

  def findBankAccountWithAccountNumber(bankAccountNumber: String) : Option[BankAccount] = {
    accounts.find(_.accountNumber == bankAccountNumber)
  }

  def clear() {
    accounts = List()
  }

  def create(bankAccount: BankAccount) {
    if(accounts.exists(_.equals(bankAccount))) {
      throw new BankAccountAlreadyExists
    }
    accounts = bankAccount :: accounts
  }

}
