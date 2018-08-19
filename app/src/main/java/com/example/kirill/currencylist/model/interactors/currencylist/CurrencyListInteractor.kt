package com.example.kirill.currencylist.model.interactors.currencylist

import com.example.kirill.currencylist.model.repository.currencylist.CurrencyListRepository

class CurrencyListInteractor(private val repository: CurrencyListRepository) {

    fun observeRates(baseCurrency: String, value: String?) = repository.getRates(baseCurrency, value)
}