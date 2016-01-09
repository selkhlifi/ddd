package com.github.selkhlifi.ddd.bank.domain

import java.util.Currency

class Bank {
  type ExchangeRate = (Currency, Currency, Double)
  val MAD: Currency = Currency.getInstance("MAD")
  val EURO: Currency = Currency.getInstance("EURO")

  var sellingRates: List[ExchangeRate] = List(
    (MAD, EURO, 10.798),//the bank sells 1 € with 10.798 MAD
    (EURO, MAD, 0.10) //
  )
  var buyingRates: List[ExchangeRate] = List(
    (MAD, EURO, 10.733),// the bank buys 1 € with 10.733 MAD
    (EURO, MAD, 0.08)
  )
}
