package com.example.kirill.currencylist.presentation.currencylist

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.example.kirill.currencylist.model.interactors.currencylist.CurrencyListInteractor
import com.example.kirill.currencylist.utils.DEFAULT_BASE_CURRENCY
import io.reactivex.disposables.Disposable

@InjectViewState
class CurrencyListPresenter(private val interactor: CurrencyListInteractor) : MvpPresenter<CurrencyListView>() {

    companion object {
        val LOG_TAG = CurrencyListPresenter::class.java.simpleName
    }

    private var disposable: Disposable? = null
    private var currentBaseCurrencyItemUnit = CurrencyItemUnit(DEFAULT_BASE_CURRENCY, 1.toString())

    override fun onFirstViewAttach() = startObserving()

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    fun onCurrencyItemClicked(clickedCurrencyItemUnit: CurrencyItemUnit, positon: Int) {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
        currentBaseCurrencyItemUnit = clickedCurrencyItemUnit
        viewState.moveBaseItem(clickedCurrencyItemUnit, positon)
    }

    fun onItemMoved(currencyItemUnit: CurrencyItemUnit) = startObserving(currencyItemUnit.currencyCode, currencyItemUnit.currencyValue)

    fun onValueChanged(itemUnit: CurrencyItemUnit) = startObserving(itemUnit.currencyCode, itemUnit.currencyValue)

    private fun startObserving(currency: String = DEFAULT_BASE_CURRENCY, value: String? = null) {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
        Log.d(LOG_TAG, "base: ${currency} value ${value}")
        disposable = interactor
                .observeRates(
                        baseCurrency = currency,
                        value = value
                )
                .subscribe(
                        { currencyList -> viewState.updateCurrencyList(currentBaseCurrencyItemUnit, currencyList) },
                        { e -> viewState.handleError(e) }
                )
    }

    fun onRetryClicked() = startObserving(currentBaseCurrencyItemUnit.currencyCode, currentBaseCurrencyItemUnit.currencyValue)
}