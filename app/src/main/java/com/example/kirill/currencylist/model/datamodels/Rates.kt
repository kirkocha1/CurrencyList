package com.example.kirill.currencylist.model.datamodels

import com.google.gson.annotations.SerializedName
import java.util.*

data class Rates(
        @SerializedName("date") val date: Date,
        @SerializedName("base") val baseCurrency: String,
        @SerializedName("rates") val rates: Map<String, String>
)