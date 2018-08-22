package com.example.kirill.currencylist.model.repository.currencylist

import com.example.kirill.currencylist.model.datamodels.RateToCurrencyConverter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CurrencyListRepository @Inject constructor(
        private val api: CurrencyApi,
        private val converter: RateToCurrencyConverter
) {

    fun getRates(baseCurrency: String, value: BigDecimal?): Observable<Map<String, BigDecimal>> = Observable
            .timer(1, TimeUnit.SECONDS)
            .concatMap {
                api
                        .getRates(baseCurrency, value?.toString())
                        .map { rates ->
                            converter.convertToMap(rates)
                        }
                        .toObservable()
            }
            .repeat()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}