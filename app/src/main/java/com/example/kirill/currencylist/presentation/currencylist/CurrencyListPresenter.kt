package com.example.kirill.currencylist.presentation.currencylist

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.example.kirill.currencylist.model.interactors.currencylist.BaseCurrencyInMap
import com.example.kirill.currencylist.model.interactors.currencylist.CurrencyListInteractor
import com.example.kirill.currencylist.utils.DEFAULT_BASE_CURRENCY
import com.example.kirill.currencylist.view.currencylist.CurrencyListFragment
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@InjectViewState
class CurrencyListPresenter @Inject constructor(
        private val interactor: CurrencyListInteractor
) : MvpPresenter<CurrencyListView>() {

    companion object {
        val LOG_TAG = CurrencyListPresenter::class.java.simpleName
    }

    private var disposable: Disposable? = null
    private var currentBaseCurrencyItemUnit = CurrencyItemUnit(DEFAULT_BASE_CURRENCY, 1.toString())

    override fun onFirstViewAttach() = startObserving()

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        CurrencyListFragment.clearDependency()
    }

    fun onCurrencyItemClicked(clickedCurrencyItemUnit: CurrencyItemUnit, positon: Int) {
        currentBaseCurrencyItemUnit = clickedCurrencyItemUnit
        Log.e(LOG_TAG, "onCurrencyItemClicked ${currentBaseCurrencyItemUnit.currencyCode} ${currentBaseCurrencyItemUnit.currencyValue}")
        startObserving(currentBaseCurrencyItemUnit.currencyCode, currentBaseCurrencyItemUnit.currencyValue)
    }

    fun onBaseCurrencyValueChanged(itemUnit: CurrencyItemUnit) {
        Log.e(LOG_TAG, "onBaseCurrencyValueChanged ${itemUnit.currencyCode} ${itemUnit.currencyValue}")
        startObserving(itemUnit.currencyCode, itemUnit.currencyValue)
    }

    private fun startObserving(currency: String = DEFAULT_BASE_CURRENCY, value: String? = null) {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
        Log.d(LOG_TAG, "startObserving base: ${currency} value ${value}")
        disposable = interactor
                .observeRates(
                        baseCurrency = currency,
                        value = value
                )
                .flatMap { rates -> interactor.validateRates(rates, currentBaseCurrencyItemUnit) }
                .subscribe(
                        { currencyMap ->
                            Log.e(LOG_TAG, "startObserving result ${currentBaseCurrencyItemUnit.currencyCode} ${currentBaseCurrencyItemUnit.currencyValue} size ${currencyMap.size}")
                            viewState.updateCurrencyList(currentBaseCurrencyItemUnit, currencyMap)
                        },
                        { e -> processError(e) }
                )
    }

    private fun processError(error: Throwable) {
        when {
            error is BaseCurrencyInMap -> {
                Log.e(LOG_TAG, error.localizedMessage)
                startObserving(currentBaseCurrencyItemUnit.currencyCode, currentBaseCurrencyItemUnit.currencyValue)
            }
            else -> {
                viewState.handleError(error)
            }
        }
    }

    fun onRetryClicked() = startObserving(currentBaseCurrencyItemUnit.currencyCode, currentBaseCurrencyItemUnit.currencyValue)
}