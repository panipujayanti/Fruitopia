package com.group4.fruitopia.utils

import java.text.NumberFormat
import java.util.Currency

fun Int.toCurrencyFormat(): String {
    val format: android.icu.text.NumberFormat = android.icu.text.NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 0
    format.currency = android.icu.util.Currency.getInstance("IDR")
    return format.format(this)
}