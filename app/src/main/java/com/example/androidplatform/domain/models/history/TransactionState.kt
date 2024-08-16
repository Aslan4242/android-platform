package com.example.androidplatform.domain.models.history

enum class TransactionState(val value: String) {
    HOLD("Hold"),
    COMPLETED("Completed"),
    CANCELED("Canceled")
}
