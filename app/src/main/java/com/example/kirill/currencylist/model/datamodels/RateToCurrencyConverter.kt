package com.example.kirill.currencylist.model.datamodels

class RateToCurrencyConverter {

    fun convert(rates: Rates): List<CurrencyItemUnit> = mutableListOf<CurrencyItemUnit>().apply {
        rates.rates.forEach { code, value ->
            add(CurrencyItemUnit(code, value))
        }
    }

    fun convertToMap(rates: Rates) = rates.rates.toMap()
}

data class BaseWithRates(
        val baseCurrency: String,
        val rates: Map<String, String>
)
