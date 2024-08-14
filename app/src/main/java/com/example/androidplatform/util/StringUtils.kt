package com.example.androidplatform.util

import com.example.androidplatform.domain.models.account.Currency
import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

fun String.toMoneyFormat(): String {
    val stringBuilder = StringBuilder(this)
    var counter = 0
    for (i in stringBuilder.length downTo 0) {
        if (counter % 3 == 0 && counter != 0 && i != 0) stringBuilder.insert(i, " ")
        counter++
    }
    return stringBuilder.toString()
}

fun String.toCurrencyMoneyFormat(currencyCode: Short): String {
    return when (currencyCode) {
        Currency.RUB.code -> "${this.toMoneyFormat()} ₽"
        Currency.USD.code -> "${this.toMoneyFormat()} $"
        Currency.EUR.code -> "${this.toMoneyFormat()} €"
        Currency.CNY.code -> "${this.toMoneyFormat()} ¥"
        else -> this.toMoneyFormat()
    }
}

fun String.parseDate(): LocalDateTime {
    val formatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd")
        .optionalStart()
        .appendPattern("'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 6, true)
        .optionalEnd()
        .optionalEnd()
        .toFormatter()

    return LocalDateTime.parse(this, formatter)
}
