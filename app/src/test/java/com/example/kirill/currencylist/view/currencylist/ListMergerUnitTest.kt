package com.example.kirill.currencylist.view.currencylist

import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import org.junit.Test
import java.math.BigDecimal

class ListMergerUnitTest {

    @Test
    fun check_result_list_cointains_same_items_and_new_items_in_the_end() {
        val merger = ListMerger()
        val testPrevList = constructList()
        val newTestDataMap = constructMap()
        val result = merger.constructUpdatedList(testPrevList, newTestDataMap)
        for (i in 0..testPrevList.size) {
            assert(result[i].currencyCode == testPrevList[i].currencyCode)
        }
        for (i in testPrevList.size - 1..result.size) {
            assert(testPrevList.find { result[i].currencyCode == it.currencyCode } == null)
        }
    }

    @Test
    fun check_result_list_contains_only_repeated_and_new_elements_from_map() {
        val merger = ListMerger()
        val testPrevList = constructList()
        val newTestDataMap = constructMap()
        val result = merger.constructUpdatedList(testPrevList, newTestDataMap)
        for (i in 0..result.size) {
            assert(result[i].currencyCode != "FAKE")
        }
    }
    
    private fun constructList() = mutableListOf<CurrencyItemUnit>().apply {
        add(CurrencyItemUnit("EUR", 1.toBigDecimal()))
        add(CurrencyItemUnit("BGN", 1.toBigDecimal()))
        add(CurrencyItemUnit("BRL", 1.toBigDecimal()))
        add(CurrencyItemUnit("CAD", 1.toBigDecimal()))
        add(CurrencyItemUnit("CHF", 1.toBigDecimal()))
        add(CurrencyItemUnit("CNY", 1.toBigDecimal()))
    }

    private fun constructAnotherList() = mutableListOf<CurrencyItemUnit>().apply {
        add(CurrencyItemUnit("EUR", 1.toBigDecimal()))
        add(CurrencyItemUnit("BGN", 1.toBigDecimal()))
        add(CurrencyItemUnit("BRL", 1.toBigDecimal()))
        add(CurrencyItemUnit("CAD", 1.toBigDecimal()))
        add(CurrencyItemUnit("CHF", 1.toBigDecimal()))
        add(CurrencyItemUnit("CNY", 1.toBigDecimal()))
        add(CurrencyItemUnit("FAKE", 1.toBigDecimal()))
    }

    private fun constructMap() = mutableMapOf<String, BigDecimal>().apply {
        put("CNY", 1.toBigDecimal())
        put("BGN", 1.toBigDecimal())
        put("BRL", 1.toBigDecimal())
        put("EUR", 1.toBigDecimal())
        put("CAD", 1.toBigDecimal())
        put("CHF", 1.toBigDecimal())
        put("USD", 1.toBigDecimal())
        put("ZAR", 1.toBigDecimal())
        put("CZK", 1.toBigDecimal())
    }

}