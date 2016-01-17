package com.github.selkhlifi.ddd.bank.services

import java.util.Currency

import com.github.selkhlifi.ddd.bank.domain.money.Money
import com.github.selkhlifi.ddd.bank.repositories.ExchangeRateRepository

class ExchangeService {

  def registerExchangeRate(from: Currency, to: Currency, rate: Double) = {
    ExchangeRateRepository.register(from, to, rate)
  }

  def change(money: Money, to: Currency) = {
    val from = money.currency
    val rate = ExchangeRateRepository.get(from, to)
    Money(money.amount * rate, to)
  }
}