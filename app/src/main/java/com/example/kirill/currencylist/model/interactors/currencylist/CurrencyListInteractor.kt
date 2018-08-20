package com.example.kirill.currencylist.model.interactors.currencylist

import com.example.kirill.currencylist.model.repository.currencylist.CurrencyListRepository
import io.reactivex.Observable

class CurrencyListInteractor(private val repository: CurrencyListRepository) {

    fun observeRates(baseCurrency: String, value: String?): Observable<Map<String, String>> {
        return repository.getRates(baseCurrency, value)
    }
}