package com.example.kirill.currencylist.view.currencylist

import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import java.math.BigDecimal

class ListMerger {

    fun constructUpdatedList(previousList: MutableList<CurrencyItemUnit>, newCurrencyMap: Map<String, BigDecimal>): MutableList<CurrencyItemUnit> {
        val repeatedItems = mutableMapOf<String, BigDecimal>()
        val newNonRepeatedItems = mutableListOf<CurrencyItemUnit>()
        newCurrencyMap.forEach { code, value ->
            if (previousList.find { it.currencyCode == code } != null) {
                repeatedItems[code] = value
            } else {
                newNonRepeatedItems.add(CurrencyItemUnit(code, value))
            }
        }
        val validList = previousList.filter { repeatedItems[it.currencyCode] != null }.toMutableList()
        validList.addAll(newNonRepeatedItems.map { (code, value) -> CurrencyItemUnit(code, value) })
        return validList
    }
}