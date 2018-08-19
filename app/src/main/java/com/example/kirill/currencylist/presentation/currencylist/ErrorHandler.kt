package com.example.kirill.currencylist.presentation.currencylist

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface ErrorHandler {
    @StateStrategyType(SkipStrategy::class)
    fun handleError(error: Throwable) = Unit
}