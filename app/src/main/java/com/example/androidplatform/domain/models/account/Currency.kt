package com.example.androidplatform.domain.models.account

enum class Currency(val code: Int, val currencyName: String) {
    RUB(643, "Российский Рубль"),
    USD(840, "Доллар США"),
    EUR(978, "Евро"),
    CNY(156, "Китайский Юань")
}
