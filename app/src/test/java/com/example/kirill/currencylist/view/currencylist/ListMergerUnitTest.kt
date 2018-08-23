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
        for (i in 0 until testPrevList.size) {
            assert(result[i].currencyCode == testPrevList[i].currencyCode)
        }
        for (i in testPrevList.size until result.size) {
            assert(testPrevList.find { result[i].currencyCode == it.currencyCode } == null)
        }
    }

    @Test
    fun check_result_list_contains_only_repeated_and_new_elements_from_map() {
        val merger = ListMerger()
        val testPrevList = constructList()
        val newTestDataMap = constructMap()
        val result = merger.constructUpdatedList(testPrevList, newTestDataMap)
        for (i in 0 until result.size) {
            assert(result[i].currencyCode != "FAKE")
        }
    }

    @Test
    fun check_result_list_contains_only_same_updated_item_from_map_with_same_county_codes() {
        val merger = ListMerger()
        val testPrevList = constructList()
        val newTestDataMap = construcAnotherValueMap()
        val result = merger.constructUpdatedList(testPrevList, newTestDataMap)
        for (i in 0 until result.size) {
            assert(result[i].currencyCode == testPrevList[i].currencyCode)
            assert(result[i].currencyValue != testPrevList[i].currencyValue)
        }
    }

    @Test
    fun check_result_list_contains_same_items_in_right_direction_with_new_item_in_the_end() {
        val merger = ListMerger()
        val testPrevList = constructList()
        val newTestDataMap = construcSmallValueMap()
        val result = merger.constructUpdatedList(testPrevList, newTestDataMap)
        assert(result.size == 3)
        assert(result[0].currencyCode == "CHF")
        assert(result[1].currencyCode == "CNY")
        assert(testPrevList.find { result[2].currencyCode == it.currencyCode } == null)
    }

    private fun constructList() = mutableListOf<CurrencyItemUnit>().apply {
        add(CurrencyItemUnit("EUR", 1.toBigDecimal()))
        add(CurrencyItemUnit("BGN", 1.toBigDecimal()))
        add(CurrencyItemUnit("BRL", 1.toBigDecimal()))
        add(CurrencyItemUnit("CAD", 1.toBigDecimal()))
        add(CurrencyItemUnit("CHF", 1.toBigDecimal()))
        add(CurrencyItemUnit("CNY", 1.toBigDecimal()))
    }


    private fun construcAnotherValueMap() = mutableMapOf<String, BigDecimal>().apply {
        put("CNY", 2.toBigDecimal())
        put("BGN", 2.toBigDecimal())
        put("BRL", 2.toBigDecimal())
        put("EUR", 2.toBigDecimal())
        put("CAD", 2.toBigDecimal())
        put("CHF", 2.toBigDecimal())
    }

    private fun construcSmallValueMap() = mutableMapOf<String, BigDecimal>().apply {
        put("CNY", 2.toBigDecimal())
        put("CHF", 2.toBigDecimal())
        put("CZK", 2.toBigDecimal())
    }

    private fun constructMap() = mutableMapOf<String, BigDecimal>().apply {
        put("CNY", 2.toBigDecimal())
        put("BGN", 2.toBigDecimal())
        put("BRL", 2.toBigDecimal())
        put("EUR", 2.toBigDecimal())
        put("CAD", 2.toBigDecimal())
        put("CHF", 2.toBigDecimal())
        put("USD", 2.toBigDecimal())
        put("ZAR", 2.toBigDecimal())
        put("CZK", 2.toBigDecimal())
    }

}