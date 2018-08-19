package com.example.kirill.currencylist.view

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.example.kirill.currencylist.R
import com.example.kirill.currencylist.view.currencylist.CurrencyListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstanceState ?: init()
    }

    private fun init() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, CurrencyListFragment.newInstance(), CurrencyListFragment::class.java.simpleName)
                .commit()
    }
}
