package com.example.kirill.currencylist.model.repository.currencylist

import com.example.kirill.currencylist.model.datamodels.RateToCurrencyConverter
import com.example.kirill.currencylist.model.datamodels.Rates
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class CurrencyListRepository(private val api: CurrencyApi, private val converter: RateToCurrencyConverter) {

    fun getRates(baseCurrency: String, value: String?) = Observable
            .timer(1, TimeUnit.SECONDS)
            .concatMap {
                api
                        .getRates(baseCurrency, value)
                        .map { rates ->
                            converter.convert(rates)
                        }
                        .toObservable()
            }
            .repeat()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}