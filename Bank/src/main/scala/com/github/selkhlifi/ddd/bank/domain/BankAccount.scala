package com.github.selkhlifi.ddd.bank.domain

class BankAccount extends Cloneable {

  type Balance = BigDecimal
  var accountNumber: String = _

  var balance: Balance = 0.0

  //require : to test whether parameters of methods have legitimate values.
  //assume : to ensure that the result of methods are as expected.
  def withdraw(amount: BigDecimal) = {
    require(amount >= 0, "must withdraw positive amounts")
    assume(balance - amount >= 0, "overdrafts not allowed")

    balance = balance - amount
  }

  def deposit(amount: BigDecimal) = {
    require(amount >= 0.0, "must deposit positive amounts")
    balance = balance + amount
  }

  override def equals(obj: scala.Any): Boolean = {
    this.accountNumber == obj.asInstanceOf[BankAccount].accountNumber
  }

  override def clone(): BankAccount = {
    val clone = new BankAccount
    clone.balance = this.balance
    clone.accountNumber = this.accountNumber
    clone
  }
}
