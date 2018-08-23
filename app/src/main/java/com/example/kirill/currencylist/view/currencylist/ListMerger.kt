package com.example.kirill.currencylist.view.currencylist

import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import java.math.BigDecimal

class ListMerger {

    fun constructUpdatedList(previousList: MutableList<CurrencyItemUnit>, newCurrencyMap: Map<String, BigDecimal>): MutableList<CurrencyItemUnit> {
        val repeatedItems = newCurrencyMap.filter { (code, _) -> previousList.find { it.currencyCode == code } != null }
        val newNonRepeatedItems = newCurrencyMap.filter { (code, _) -> repeatedItems[code] == null }
        val validList = previousList
                .filter { repeatedItems[it.currencyCode] != null }
                .map { CurrencyItemUnit(it.currencyCode, newCurrencyMap[it.currencyCode]!!) }
                .toMutableList()
        validList.addAll(newNonRepeatedItems.map { (code, value) -> CurrencyItemUnit(code, value) })
        return validList
    }
}