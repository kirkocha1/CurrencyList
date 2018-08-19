package com.example.kirill.currencylist.model.repository.currencylist

import com.example.kirill.currencylist.BuildConfig
import com.example.kirill.currencylist.model.datamodels.Rates
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.temporal.TemporalAmount


interface CurrencyApi {

    companion object {
        const val LATEST = "latest"
    }

    //Use Single as retrun value because it is rest api call
    @GET(LATEST)
    fun getRates(@Query("base") baseCurrency: String, @Query("amount") amount: String? = null): Single<Rates>

}