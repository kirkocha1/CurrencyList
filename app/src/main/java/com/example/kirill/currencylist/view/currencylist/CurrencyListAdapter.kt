package com.example.kirill.currencylist.view.currencylist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.kirill.currencylist.R
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.example.kirill.currencylist.utils.DEFAULT_BASE_CURRENCY

class CurrencyListAdapter(
        private val itemClickListener: (CurrencyItemUnit, Int) -> Unit,
        private val itemMovedListener: (CurrencyItemUnit) -> Unit,
        private val valueChangedListener: (CurrencyItemUnit) -> Unit
) : RecyclerView.Adapter<CurrencyItemViewHolder>() {

    private var currencyItems = mutableListOf<CurrencyItemUnit>()
    private var baseCurrencyItemUnit: CurrencyItemUnit? = null
    private var isMoving = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemViewHolder =
            CurrencyItemViewHolder(
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false),
                    clickListener = ::resolveClick,
                    valueChangedListener = { value -> valueChangedListener(value) }
            )

    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int) {
        if (position != 0) {
            holder.disableCurrencyInput()
        } else {
            holder.enableCurrencyInput()
        }
        holder.bind(currencyItems[position])
    }

    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        }
    }

    override fun getItemCount() = currencyItems.size

    fun renderList(baseCurrencyUnit: CurrencyItemUnit, currencyList: MutableList<CurrencyItemUnit>) {
        if (baseCurrencyItemUnit == null) {
            baseCurrencyItemUnit = baseCurrencyUnit
            createList(currencyList)
        } else {
            updateList(currencyList)
        }
    }

    fun moveBaseCurrency(currencyItemUnit: CurrencyItemUnit, position: Int) {
        currencyItems
                .removeAt(position)
                .also { removedVal ->
                    currencyItems.add(0, removedVal)
                    baseCurrencyItemUnit = removedVal
                }
        notifyItemMoved(position, 0)
        //update items for proper work of edit text
        notifyItemChanged(0)
        notifyItemChanged(1)
        itemMovedListener(currencyItemUnit)
        isMoving = false
    }

    private fun createList(currencyList: MutableList<CurrencyItemUnit>) {
        baseCurrencyItemUnit = CurrencyItemUnit(DEFAULT_BASE_CURRENCY, 1.toString()).also { currencyItems.add(it) }
        currencyItems.addAll(1, currencyList)
        notifyDataSetChanged()
    }

    private fun updateList(currencyList: MutableList<CurrencyItemUnit>) {
        if (!isMoving) {
            currencyList.forEach { newCurrency ->
                currencyItems
                        .find { newCurrency.currencyCode == it.currencyCode }
                        ?.let { it.currencyValue = newCurrency.currencyValue }
                notifyItemRangeChanged(1, itemCount - 1)
            }
        }
    }

    private fun resolveClick(currencyItemUnit: CurrencyItemUnit, position: Int) {
        position
                .takeIf { it > 0 }
                ?.let {
                    isMoving = true
                    itemClickListener(currencyItemUnit, position)
                }
    }
}