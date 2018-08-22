package com.example.kirill.currencylist.utils

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun ui(): Scheduler
    fun io(): Scheduler
}