package com.example.kirill.currencylist.view.currencylist

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.kirill.currencylist.R
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit

class CurrencyListAdapter(
        private val savedInstanceState: Bundle?,
        private val itemClickListener: (CurrencyItemUnit, Int) -> Unit,
        private val valueChangedListener: (CurrencyItemUnit) -> Unit
) : RecyclerView.Adapter<CurrencyItemViewHolder>() {

    companion object {
        const val KEY_CURRENCY_LIST = "key_currency_list"
    }

    private var currencyItems = mutableListOf<CurrencyItemUnit>()
    private var baseCurrencyItemUnit: CurrencyItemUnit? = null

    init {
        savedInstanceState
                ?.getParcelableArrayList<CurrencyItemUnit>(KEY_CURRENCY_LIST)
                ?.let { currencyItems = it }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemViewHolder =
            CurrencyItemViewHolder(
                    view = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false),
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
        when {
            payloads.isEmpty() -> onBindViewHolder(holder, position)
            payloads[0] is PayloadData -> processWithPayloads(holder, (payloads[0] as PayloadData).map, position)
        }
    }

    override fun getItemCount() = currencyItems.size


    fun saveState(outState: Bundle) {
        if (itemCount != 0) {
            outState.putParcelableArrayList(KEY_CURRENCY_LIST, ArrayList(currencyItems))
        }
    }

    fun renderList(baseCurrencyUnit: CurrencyItemUnit, currencyListMap: Map<String, String>) {
        baseCurrencyItemUnit = baseCurrencyUnit
        if (itemCount == 0) {
            createList(baseCurrencyUnit, currencyListMap.map { (code, value) -> CurrencyItemUnit(code, value) })
        } else {
            updateList(currencyListMap)
        }
    }

    private fun processWithPayloads(holder: CurrencyItemViewHolder, map: Map<String, String>, position: Int) {
        with(currencyItems[position]) {
            map[this.currencyCode]?.let { holder.bind(this, it) }
        }
    }

    private fun createList(baseCurrencyUnit: CurrencyItemUnit, currencyList: List<CurrencyItemUnit>) {
        currencyItems.add(baseCurrencyUnit)
        currencyItems.addAll(1, currencyList)
        notifyDataSetChanged()
    }

    private fun updateList(currencyList: Map<String, String>) {
        notifyItemRangeChanged(1, itemCount - 1, PayloadData(currencyList))
    }

    private fun resolveClick(currencyItemUnit: CurrencyItemUnit, position: Int) {
        position
                .takeIf { it > 0 }
                ?.let {
                    itemClickListener(currencyItemUnit, position)
                    currencyItems
                            .removeAt(position)
                            .also { removedVal ->
                                currencyItems.add(0, removedVal)
                                baseCurrencyItemUnit = removedVal
                            }
                    itemClickListener(currencyItemUnit, position)
                    notifyItemMoved(position, 0)
                    //update items for proper work of edit text
                    notifyItemChanged(0)
                    notifyItemChanged(1)
                }
    }

    internal data class PayloadData(val map: Map<String, String>)
}