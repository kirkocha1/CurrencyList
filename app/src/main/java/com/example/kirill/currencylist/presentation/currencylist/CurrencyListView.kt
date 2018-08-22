package com.example.kirill.currencylist.presentation.currencylist

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit

interface CurrencyListView : ErrorHandler, MvpView {

    @StateStrategyType(SkipStrategy::class)
    fun updateCurrencyList(baseCurrency: CurrencyItemUnit, currencyMap: Map<String, String>)

}