package com.example.kirill.currencylist.model.datamodels

class RateToCurrencyConverter {

    fun convertToMap(rates: Rates) = rates.rates.mapValues { rawEntry -> rawEntry.value.toBigDecimal() }
}
