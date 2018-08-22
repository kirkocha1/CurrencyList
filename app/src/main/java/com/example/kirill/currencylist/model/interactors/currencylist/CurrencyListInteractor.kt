package com.example.kirill.currencylist.model.interactors.currencylist

import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.example.kirill.currencylist.model.repository.currencylist.CurrencyListRepository
import io.reactivex.Observable
import java.math.BigDecimal
import java.math.BigInteger
import javax.inject.Inject

class CurrencyListInteractor @Inject constructor(private val repository: CurrencyListRepository) {

    fun observeRates(baseCurrency: String, value: BigDecimal?): Observable<Map<String, BigDecimal>> = when {
        value != null && value < BigDecimal(BigInteger.ZERO) -> Observable.error<Map<String, BigDecimal>>(CurrencyWrongValueException("value is less than zero"))
        else -> repository.getRates(baseCurrency, value)
    }

    fun validateRates(rates: Map<String, BigDecimal>, currentBaseCurrencyItemUnit: CurrencyItemUnit): Observable<Map<String, BigDecimal>> = Observable
            .fromCallable {
                when {
                    rates[currentBaseCurrencyItemUnit.currencyCode] != null -> throw BaseCurrencyInMapException("error current base currency in list")
                    else -> rates
                }
            }
}

class CurrencyWrongValueException(errorMessage: String) : Exception(errorMessage)
class BaseCurrencyInMapException(errorMessage: String) : Exception(errorMessage)