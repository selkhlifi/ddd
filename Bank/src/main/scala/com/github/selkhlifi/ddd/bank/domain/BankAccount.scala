package com.github.selkhlifi.ddd.bank.domain

import java.util.Currency

import com.github.selkhlifi.ddd.bank.domain.money.Money

class BankAccount(val currency: Currency) extends Cloneable {
  type Balance  = BigDecimal

  var balance: Money = Money(0.0, currency)

  var accountNumber: String = _

  //require : to test whether parameters of methods have legitimate values.
  //assume : to ensure that the result of methods are as expected.
  def withdraw(money: Money) = {
    require(money.amount >= 0.0, "must withdraw positive amounts")
    require(money.currency == currency, "must withdraw same currency")
    assume(balance.amount - money.amount >= 0, "overdrafts not allowed")

    balance = balance.subtract(money)
  }

  def deposit(money: Money) = {
    require(money.amount >= 0.0, "must deposit positive amounts")
    require(money.currency == currency, "must deposit same currency")

    balance = balance.add(money)
  }

  override def equals(obj: scala.Any): Boolean = {
    this.accountNumber == obj.asInstanceOf[BankAccount].accountNumber
  }

  override def clone(): BankAccount = {
    val clone = new BankAccount(currency)
    clone.balance = this.balance
    clone.accountNumber = this.accountNumber
    clone
  }
}
