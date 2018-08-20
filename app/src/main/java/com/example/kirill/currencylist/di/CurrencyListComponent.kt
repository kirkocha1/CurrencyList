package com.example.kirill.currencylist.di

import com.example.kirill.currencylist.presentation.currencylist.CurrencyListPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(CurrencyListModule::class))
interface CurrencyListComponent {

    fun providePresenter(): CurrencyListPresenter

}
