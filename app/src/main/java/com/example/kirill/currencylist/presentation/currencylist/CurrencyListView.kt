package com.example.kirill.currencylist.presentation.currencylist

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit

interface CurrencyListView : ErrorHandler, MvpView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateCurrencyList(currencyList: List<CurrencyItemUnit>)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun moveBaseItem(currency: CurrencyItemUnit, positon: Int)
}