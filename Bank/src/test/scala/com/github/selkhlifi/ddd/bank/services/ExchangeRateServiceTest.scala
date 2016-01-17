package com.github.selkhlifi.ddd.bank.services

import java.util.Currency

import com.github.selkhlifi.ddd.bank.domain.money.Money
import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExchangeRateServiceTest extends FunSuite with BeforeAndAfterEach {
  var exchangeService : ExchangeService = _

  override protected def beforeEach() = {
    exchangeService = new ExchangeService()
  }

  test("It should be possible to register an exchange rate from one currency to another currency") {
    val from = Currency.getInstance("MAD")
    val to = Currency.getInstance("EUR")
    val rate = 10.2

    exchangeService.registerExchangeRate(from, to, rate)
    // ??
  }

  test("It should be possible to register an exchange rate from one currency to another currency and vice versa") {
    val from = Currency.getInstance("MAD")
    val to = Currency.getInstance("EUR")
    val madToEuroExchangeRate = 10.2
    val euroToMadExchangeRate = 10.4

    exchangeService.registerExchangeRate(from, to, madToEuroExchangeRate)
    exchangeService.registerExchangeRate(to, from, euroToMadExchangeRate)
  }

  test("If an exchange rate from currency A to currency B has been registered but no exchange rate" +
  "from currency B to currency A, it should be possible to exchange money in one direction only") {
    val madCurrency = Currency.getInstance("MAD")
    val euroCurrency = Currency.getInstance("EUR")
    val madToEuroExchangeRate = 10.2

    exchangeService.registerExchangeRate(madCurrency, euroCurrency, madToEuroExchangeRate)

    val madMoney = Money(500.0, madCurrency)
    val euroMoney = Money(200.2, euroCurrency)
    assert(Money(5100, euroCurrency) == exchangeService.change(madMoney, euroCurrency))

    intercept[NoExchangeRateFound] {
      exchangeService.change(euroMoney, madCurrency)
    }
  }

  test("If several exchange rates from currency A to currency B are registered, the last registered" +
  "exchange rate should be used when calculating an exchange") {
    val madCurrency = Currency.getInstance("MAD")
    val euroCurrency = Currency.getInstance("EUR")
    val madToEuroExchangeRate1 = 10.2
    val madToEuroExchangeRate2 = 10.9

    exchangeService.registerExchangeRate(madCurrency, euroCurrency, madToEuroExchangeRate1)
    exchangeService.registerExchangeRate(madCurrency, euroCurrency, madToEuroExchangeRate2)

    val madMoney = Money(500.0, madCurrency)
    assert(Money(5450.0, euroCurrency) == exchangeService.change(madMoney, euroCurrency))
  }

  test("If registering one exchange rate from currency A to B and one from currency B to A which, " +
  "when multiplied, must not be equal to 1, and then exchanging money from currency A to B" +
  "and vice versa, the resulting amount in currency A should not be equal to the original" +
  "amount in currency A.") {

    val madCurrency = Currency.getInstance("MAD")
    val euroCurrency = Currency.getInstance("EUR")
    val madToEuroExchangeRate = 10.2
    val euroToMadExchangeRate = 10.9

    exchangeService.registerExchangeRate(madCurrency, euroCurrency, madToEuroExchangeRate)
    exchangeService.registerExchangeRate(madCurrency, euroCurrency, euroToMadExchangeRate)

    val madMoney = Money(500.0, madCurrency)
    val euroMoney = exchangeService.change(madMoney, euroCurrency)

    assert(madMoney ne exchangeService.change(euroMoney, madCurrency))
  }


  test("It should always be possible to calculate the exchange from a currency A to the same" +
  "currency A. The result should be the original amount.") {
    val madCurrency = Currency.getInstance("MAD")
    val madMoney = Money(100.0, madCurrency)

    assert(madMoney eq exchangeService.change(madMoney, madCurrency))

  }

}
