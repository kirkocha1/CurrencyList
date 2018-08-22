package com.example.kirill.currencylist.view.currencylist

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.mynameismidori.currencypicker.ExtendedCurrency
import kotlinx.android.synthetic.main.currency_item.view.*
import java.math.BigDecimal

class CurrencyItemViewHolder(
        private val view: View,
        private val clickListener: (CurrencyItemUnit, Int) -> Unit,
        val valueChangedListener: (CurrencyItemUnit) -> Unit
) : RecyclerView.ViewHolder(view) {


    private val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            currentCurrencyItem?.let { itemUnit ->
                val rawVal = s?.toString()
                itemUnit.currencyValue = if (rawVal?.isNotBlank() == true) {
                    rawVal.toBigDecimal()
                } else {
                    BigDecimal.ZERO
                }
                valueChangedListener(itemUnit)
            }

        }
    }

    private var currentCurrencyItem: CurrencyItemUnit? = null

    fun bind(currencyItem: CurrencyItemUnit) {
        redrawPrimaryInfo(currencyItem)
        currentCurrencyItem = currencyItem
        with(view) {
            setOnClickListener { clickListener(currencyItem, layoutPosition) }
            currencyValueEditText.setText(currencyItem.currencyValue.toString())
            enableCurrencyValueProcessingIfNeed()
        }
    }

    fun bind(currencyItem: CurrencyItemUnit, newValue: BigDecimal) {
        currencyItem.currencyValue = newValue
        view.currencyValueEditText.setText(currencyItem.currencyValue.toString())
    }

    fun disableCurrencyInput() {
        view.currencyValueEditText.isEnabled = false
    }

    fun enableCurrencyInput() {
        view.currencyValueEditText.isEnabled = true
    }

    private fun enableCurrencyValueProcessingIfNeed() {
        with(view) {
            if (layoutPosition == 0) {
                currencyValueEditText.addTextChangedListener(watcher)
            } else {
                currencyValueEditText.removeTextChangedListener(watcher)
            }
        }
    }

    private fun redrawPrimaryInfo(currencyItem: CurrencyItemUnit) {
        view.currencyCodeTextView.text = currencyItem.currencyCode
        view.fullCurrencyNameTextView.text = ExtendedCurrency.CURRENCIES.find { it.code == currencyItem.currencyCode }?.name
        setFlagIfExists(currencyItem.currencyCode)
    }

    private fun setFlagIfExists(currenCode: String) {
        ExtendedCurrency.CURRENCIES
                .find { it.code == currenCode }
                ?.let { view.flagImageView.setImageResource(it.flag) }
    }
}