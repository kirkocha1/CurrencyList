package com.example.kirill.currencylist.view.currencylist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.kirill.currencylist.BuildConfig
import com.example.kirill.currencylist.R
import com.example.kirill.currencylist.model.datamodels.CurrencyItemUnit
import com.example.kirill.currencylist.model.datamodels.RateToCurrencyConverter
import com.example.kirill.currencylist.model.interactors.currencylist.CurrencyListInteractor
import com.example.kirill.currencylist.model.repository.currencylist.CurrencyApi
import com.example.kirill.currencylist.model.repository.currencylist.CurrencyListRepository
import com.example.kirill.currencylist.presentation.currencylist.CurrencyListPresenter
import com.example.kirill.currencylist.presentation.currencylist.CurrencyListView
import kotlinx.android.synthetic.main.fragment_currency_list.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyListFragment : MvpAppCompatFragment(), CurrencyListView {

    companion object {
        fun newInstance() = CurrencyListFragment()
    }

    @InjectPresenter
    lateinit var presenter: CurrencyListPresenter

    @ProvidePresenter
    fun providePresenter() = CurrencyListPresenter(
            CurrencyListInteractor(
                    CurrencyListRepository(
                            api = Retrofit.Builder()
                                    .baseUrl(BuildConfig.SERVER_URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                    .build()
                                    .create(CurrencyApi::class.java),
                            converter = RateToCurrencyConverter()
                    )
            )
    )

    private lateinit var adapter: CurrencyListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_currency_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CurrencyListAdapter(
                itemClickListener = { currencyValue, positon -> presenter.onCurrencyItemClicked(currencyValue, positon) },
                itemMovedListener = { currencyValue -> presenter.onItemMoved(currencyValue) },
                valueChangedListener = { value -> presenter.onValueChanged(value) }
        )
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter
    }

    override fun moveBaseItem(currency: CurrencyItemUnit, positon: Int) {
        adapter.moveBaseCurrency(currency, positon)
    }

    override fun updateCurrencyList(currencyList: List<CurrencyItemUnit>) = adapter.renderList(currencyList.toMutableList())
}