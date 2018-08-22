package com.example.kirill.currencylist.model.interactor.currencyList

import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.example.kirill.currencylist.model.interactors.currencylist.BaseCurrencyInMapException
import com.example.kirill.currencylist.model.interactors.currencylist.CurrencyListInteractor
import com.example.kirill.currencylist.model.interactors.currencylist.CurrencyWrongValueException
import com.example.kirill.currencylist.model.repository.currencylist.CurrencyListRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import java.math.BigDecimal

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class InteractorUnitTest {

    private val currencyEur = CurrencyItemUnit("EUR", BigDecimal.valueOf(1))
    private lateinit var repository: CurrencyListRepository
    private lateinit var interactor: CurrencyListInteractor

    @Before
    fun setUp() {
        repository = mock()
        interactor = CurrencyListInteractor(repository)
    }

    @Test
    fun get_rates_success() {
        whenever(repository.getRates(currencyEur.currencyCode, currencyEur.currencyValue)).thenReturn(Observable.just(constructMap()))
        val testObserver = interactor.observeRates(currencyEur.currencyCode, currencyEur.currencyValue).test()
        testObserver.awaitTerminalEvent()
        verify(repository).getRates(currencyEur.currencyCode, currencyEur.currencyValue)
        testObserver.assertNoErrors()
    }

    @Test
    fun check_validation_of_non_repeating_base_currency_item_in_map() {
        val testObserver = interactor.validateRates(constructMap(), currencyEur).test()
        testObserver.awaitTerminalEvent()
        assert(testObserver.values()[0][currencyEur.currencyCode] == null)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun check_validation_of_repeating_base_currency_item_in_map() {
        val testObserver = interactor.validateRates(constructMapWithRepat(currencyEur), currencyEur).test()
        testObserver.awaitTerminalEvent()
        testObserver.assertError(BaseCurrencyInMapException::class.java)
    }

    @Test
    fun check_only_positive_values_passed() {
        `when`(repository.getRates(ArgumentMatchers.anyString(), ArgumentMatchers.any(BigDecimal::class.java))).thenReturn(Observable.just(constructMapWithRepat(currencyEur)))
        val testObserver = interactor.observeRates(currencyEur.currencyCode, currencyEur.currencyValue).test()
        testObserver.awaitTerminalEvent()
        assert(testObserver.values()[0].isNotEmpty())
        testObserver
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun check_negative_values_errors() {
        `when`(repository.getRates(ArgumentMatchers.anyString(), ArgumentMatchers.any(BigDecimal::class.java))).thenReturn(Observable.just(constructMapWithRepat(currencyEur)))
        val testObserver = interactor.observeRates(currencyEur.currencyCode, BigDecimal.valueOf(-1)).test()
        testObserver.awaitTerminalEvent()
        testObserver
                .assertError(CurrencyWrongValueException::class.java)
    }

    private fun constructMap() = mutableMapOf<String, BigDecimal>().apply {
        put("AUD", "1.5785".toBigDecimal())
        put("BGN", "1.9558".toBigDecimal())
        put("BRL", "4.6913".toBigDecimal())
        put("CAD", "1.5126".toBigDecimal())
        put("CHF", "1.1401".toBigDecimal())
    }

    private fun constructMapWithRepat(currencyUnit: CurrencyItemUnit) = mutableMapOf<String, BigDecimal>().apply {
        put(currencyUnit.currencyCode, currencyUnit.currencyValue ?: "1".toBigDecimal())
        put("AUD", "1.5785".toBigDecimal())
        put("BGN", "1.9558".toBigDecimal())
        put("BRL", "4.6913".toBigDecimal())
        put("CAD", "1.5126".toBigDecimal())
        put("CHF", "1.1401".toBigDecimal())
    }
}
