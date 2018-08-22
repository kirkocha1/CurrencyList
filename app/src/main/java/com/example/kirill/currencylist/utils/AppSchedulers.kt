package com.example.kirill.currencylist.utils

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppSchedulers : SchedulersProvider {

    override fun ui() = AndroidSchedulers.mainThread()

    override fun io() = Schedulers.io()
}