package com.github.selkhlifi.ddd.bank.domain.money

import java.util.Currency

case class Money(amount: BigDecimal, currency: Currency) {

  def +(that: Money) = {
    require(that.currency == currency,
      "must add same currency money")
    new Money(amount + that.amount, currency)
  }

  def -(that: Money) = {
    require(that.currency == currency,
      "must add subtract currency money")
    new Money(amount - that.amount, currency)
  }
}
