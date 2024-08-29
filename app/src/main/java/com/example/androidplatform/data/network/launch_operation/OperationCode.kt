package com.example.androidplatform.data.network.launch_operation

enum class OperationCode(val description: String) {
    CARD_ORDER("CardOrder"),
    ACCOUNT_OPEN("AccountOpen"),
    ACCOUNT_TRANSFER("AccountTransfer"),
    ACCOUNT_REFILL("AccountRefill")
}
