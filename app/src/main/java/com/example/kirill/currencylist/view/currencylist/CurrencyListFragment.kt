package com.example.kirill.currencylist.view.currencylist

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.kirill.currencylist.R
import com.example.kirill.currencylist.di.ComponentHolder
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.example.kirill.currencylist.presentation.currencylist.CurrencyListPresenter
import com.example.kirill.currencylist.presentation.currencylist.CurrencyListView
import kotlinx.android.synthetic.main.fragment_currency_list.*

class CurrencyListFragment : MvpAppCompatFragment(), CurrencyListView {

    companion object {

        fun newInstance() = CurrencyListFragment()

        fun clearDependency() = ComponentHolder.destroyComponent()
    }

    @InjectPresenter
    lateinit var presenter: CurrencyListPresenter

    @ProvidePresenter
    fun providePresenter() = ComponentHolder.getCurrencyListComponent().providePresenter()

    private lateinit var adapter: CurrencyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null || !ComponentHolder.isExist()) {
            ComponentHolder.createComponent()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_currency_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CurrencyListAdapter(
                itemClickListener = { currencyValue, positon ->
                    (list.layoutManager as? LinearLayoutManager)?.scrollToPosition(0)
                    presenter.onCurrencyItemClicked(currencyValue, positon)
                },
                valueChangedListener = { value -> presenter.onBaseCurrencyValueChanged(value) }
        )
        list.itemAnimator = object : DefaultItemAnimator() {
            override fun onMoveStarting(item: RecyclerView.ViewHolder?) {
                super.onMoveStarting(item)
                Log.e("itemAnimator", "onMoveStarting")
            }


            override fun onMoveFinished(item: RecyclerView.ViewHolder?) {
//                Log.e("itemAnimator", "onMoveFinised")
            }

            override fun onChangeStarting(item: RecyclerView.ViewHolder?, oldItem: Boolean) {
                super.onChangeStarting(item, oldItem)
                Log.e("itemAnimator", "onMoveStarting")
            }

            override fun onChangeFinished(item: RecyclerView.ViewHolder?, oldItem: Boolean) {
//                Log.e("ItemAnimator", "onChangedFinished")
            }
        }
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
    }

    override fun updateCurrencyList(baseCurrency: CurrencyItemUnit, currencyMap: Map<String, String>) =
            adapter.renderList(baseCurrency, currencyMap)

    override fun handleError(error: Throwable) {
        with(Snackbar.make(listContainer, error.localizedMessage, Snackbar.LENGTH_SHORT)) {
            setAction(R.string.retry, { presenter.onRetryClicked() })
            show()
        }
    }
}