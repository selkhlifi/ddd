package com.github.selkhlifi.ddd.bank.repositories

import java.util.Currency

import com.github.selkhlifi.ddd.bank.services.NoExchangeRateFound


object ExchangeRateRepository {

  type ExchangeRate = (Currency, Currency, Double)

  var exchangeRates : List[ExchangeRate] = List()


  def register(from: Currency, to: Currency, rate: Double) = {
    exchangeRates = (from, to, rate) :: exchangeRates
  }

  def get(from: Currency, to: Currency) = {
    val (_, _, rate) = exchangeRates.find({ case (c1, c2, _) => (c1, c2) == (from, to) }).getOrElse(throw new NoExchangeRateFound)
    rate
  }

}
