package com.example.kirill.currencylist.view.currencylist

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.kirill.currencylist.R
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import java.math.BigDecimal
import java.util.*

class CurrencyListAdapter(
        private val savedInstanceState: Bundle?,
        private val itemClickListener: (CurrencyItemUnit, Int) -> Unit,
        private val valueChangedListener: (CurrencyItemUnit) -> Unit
) : RecyclerView.Adapter<CurrencyItemViewHolder>() {

    companion object {
        const val KEY_CURRENCY_LIST = "key_currency_list"
    }

    private var currencyItems = mutableListOf<CurrencyItemUnit>()
    private var listMerger = ListMerger()

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
            payloads.isEmpty() -> super.onBindViewHolder(holder, position, payloads)
            else -> processWithPayloads(holder, position)
        }
    }

    override fun getItemCount() = currencyItems.size

    fun saveState(outState: Bundle) {
        if (itemCount != 0) {
            outState.putParcelableArrayList(KEY_CURRENCY_LIST, ArrayList(currencyItems))
        }
    }

    fun renderList(baseCurrencyUnit: CurrencyItemUnit, currencyListMap: Map<String, BigDecimal>) {
        if (itemCount == 0) {
            createList(baseCurrencyUnit, currencyListMap.map { (code, value) -> CurrencyItemUnit(code, value) })
        } else {
            updateList(currencyListMap)
        }
    }

    private fun processWithPayloads(holder: CurrencyItemViewHolder, position: Int) {
        holder.bind(currencyItems[position].currencyValue)
    }

    private fun createList(baseCurrencyUnit: CurrencyItemUnit, currencyList: List<CurrencyItemUnit>) {
        currencyItems.add(baseCurrencyUnit)
        currencyItems.addAll(1, currencyList)
        notifyDataSetChanged()
    }

    private fun updateList(newCurrencyMap: Map<String, BigDecimal>) {
        val newListToChange = mutableListOf<CurrencyItemUnit>().apply {
            add(currencyItems[0])
            addAll(listMerger.constructUpdatedList(currencyItems.subList(1, currencyItems.size - 1), newCurrencyMap))
        }
        val diffResult = DiffUtil.calculateDiff(CurrencyDiffCallback(currencyItems, newListToChange))
        currencyItems.clear()
        currencyItems.addAll(newListToChange)

        diffResult.dispatchUpdatesTo(this)
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
                            }
                    itemClickListener(currencyItemUnit, position)
                    notifyItemMoved(position, 0)
                    notifyItemChanged(0)
                    notifyItemChanged(1)
                }
    }

    internal class CurrencyDiffCallback(
            private val oldList: MutableList<CurrencyItemUnit>,
            private val newList: MutableList<CurrencyItemUnit>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                oldList[oldItemPosition].currencyCode == newList[newItemPosition].currencyCode

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                newList[newItemPosition].currencyValue == oldList[oldItemPosition].currencyValue

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) =
                PayloadData(true)
    }

    internal data class PayloadData(val needToUpdateValue: Boolean)
}