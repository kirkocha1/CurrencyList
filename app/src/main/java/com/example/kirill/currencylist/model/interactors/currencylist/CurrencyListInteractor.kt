package com.example.kirill.currencylist.model.interactors.currencylist

import android.util.Log
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.example.kirill.currencylist.model.repository.currencylist.CurrencyListRepository
import io.reactivex.Observable

class CurrencyListInteractor(private val repository: CurrencyListRepository) {

    fun observeRates(baseCurrency: String, value: String?): Observable<List<CurrencyItemUnit>> {
        return repository.getRates(baseCurrency, value)
    }
}