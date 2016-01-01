package com.github.selkhlifi.ddd.bank.services

class BankAccountOverdraft(val msg: String, val cause: Throwable) extends Exception(msg, cause) {
  def this() = this(null, null)
  def this(msg: String) = this(msg, null)
}
