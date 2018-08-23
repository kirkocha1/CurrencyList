package com.example.kirill.currencylist.presentation.currencylist

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.example.kirill.currencylist.model.interactors.currencylist.BaseCurrencyInMapException
import com.example.kirill.currencylist.model.interactors.currencylist.CurrencyListInteractor
import com.example.kirill.currencylist.utils.DEFAULT_BASE_CURRENCY
import com.example.kirill.currencylist.view.currencylist.CurrencyListFragment
import io.reactivex.disposables.Disposable
import java.math.BigDecimal
import javax.inject.Inject

@InjectViewState
class CurrencyListPresenter @Inject constructor(
        private val interactor: CurrencyListInteractor
) : MvpPresenter<CurrencyListView>() {

    private var disposable: Disposable? = null
    private var currentBaseCurrencyItemUnit = CurrencyItemUnit(DEFAULT_BASE_CURRENCY, BigDecimal.valueOf(1))

    override fun onFirstViewAttach() = startObserving()

    override fun onDestroy() {
        super.onDestroy()
        stopObserving()
        CurrencyListFragment.clearDependency()
    }

    fun onCurrencyItemClicked(clickedCurrencyItemUnit: CurrencyItemUnit, positon: Int) {
        currentBaseCurrencyItemUnit = clickedCurrencyItemUnit
        startObserving(currentBaseCurrencyItemUnit.currencyCode, currentBaseCurrencyItemUnit.currencyValue)
    }

    fun onBaseCurrencyValueChanged(itemUnit: CurrencyItemUnit) = if (itemUnit.currencyValue != BigDecimal.ZERO) {
        startObserving(itemUnit.currencyCode, itemUnit.currencyValue)
    } else {
        stopObserving()
    }

    private fun stopObserving() {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }

    private fun startObserving(currency: String = DEFAULT_BASE_CURRENCY, value: BigDecimal = BigDecimal.ONE) {
        stopObserving()
        disposable = interactor
                .observeRates(
                        baseCurrency = currency,
                        value = value
                )
                .flatMap { rates -> interactor.validateRates(rates, currentBaseCurrencyItemUnit) }
                .subscribe(
                        ::onGetCurrencyMap,
                        ::processError
                )
    }

    private fun onGetCurrencyMap(currencyMap: Map<String, BigDecimal>) {
        viewState.updateCurrencyList(currentBaseCurrencyItemUnit, currencyMap)
    }

    private fun processError(error: Throwable) = if (error is BaseCurrencyInMapException) {
        startObserving(currentBaseCurrencyItemUnit.currencyCode, currentBaseCurrencyItemUnit.currencyValue)
    } else {
        viewState.handleError(error)
    }

    fun onRetryClicked() = startObserving(currentBaseCurrencyItemUnit.currencyCode, currentBaseCurrencyItemUnit.currencyValue)
}