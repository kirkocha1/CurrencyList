package com.example.kirill.currencylist.model.datamodels

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable
import java.math.BigDecimal

@SuppressLint("ParcelCreator")
data class CurrencyItemUnit(
        val currencyCode: String,
        var currencyValue: BigDecimal
) : AutoParcelable