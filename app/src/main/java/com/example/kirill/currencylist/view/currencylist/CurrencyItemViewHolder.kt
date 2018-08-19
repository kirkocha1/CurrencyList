package com.example.kirill.currencylist.view.currencylist

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import kotlinx.android.synthetic.main.currency_item.view.*

class CurrencyItemViewHolder(
        private val view: View,
        private val clickListener: (CurrencyItemUnit, Int) -> Unit,
        val valueChangedListener: (CurrencyItemUnit) -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        with(view) {
            currencyValueEditText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    currencyItemUnit?.let { clickListener(it, layoutPosition) }
                }
            }
            currencyValueEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    currencyItemUnit?.let {
                        it.currencyValue = s?.toString()
                        valueChangedListener(it)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            })
            setOnClickListener { currencyValueEditText.requestFocus() }
        }
    }

    private var currencyItemUnit: CurrencyItemUnit? = null

    fun bind(currencyItem: CurrencyItemUnit) {
        currencyItemUnit = currencyItem
        with(view) {
            currencyCodeTextView.text = currencyItem.currencyCode
            currencyValueEditText.setText(currencyItem.currencyValue)
        }
    }

    fun disableCurrencyInput() {
        view.currencyValueEditText.isEnabled = false
    }

    fun enableCurrencyInput() {
        view.currencyValueEditText.isEnabled = true
    }

}