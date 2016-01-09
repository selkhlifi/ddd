package com.github.selkhlifi.ddd.bank.domain

import java.util.Currency

class Bank {
  type ExchangeRate = (Currency, Currency, Double)
  val MAD: Currency = Currency.getInstance("MAD")
  val €: Currency = Currency.getInstance("EURO")
  val $: Currency = Currency.getInstance("USD")

  var sellingRates: List[ExchangeRate] = List(
    (MAD, €, 10.798),//the bank sells 1 € with 10.798 MAD
    (€, MAD, 0.10) //
  )
  var buyingRates: List[ExchangeRate] = List(
    (MAD, €, 10.733),// the bank buys 1 € with 10.733 MAD
    (€, MAD, 0.08)
  )
}
