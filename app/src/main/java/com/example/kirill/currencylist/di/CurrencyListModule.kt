package com.example.kirill.currencylist.di

import com.example.kirill.currencylist.BuildConfig
import com.example.kirill.currencylist.model.datamodels.RateToCurrencyConverter
import com.example.kirill.currencylist.model.repository.currencylist.CurrencyApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class CurrencyListModule {

    @Provides
    @Singleton
    fun provideRetrofit() = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideCurrencyApi(retrofit: Retrofit) = retrofit.create(CurrencyApi::class.java)


    @Provides
    @Singleton
    fun provideConverter() = RateToCurrencyConverter()


}