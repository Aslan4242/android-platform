package com.example.androidplatform.domain.models.cards

enum class CardState(val value: String) {
    CREATED("Created"),
    ACTIVE("Active"),
    LOCKED("Locked"),
    EXPIRED("Expired"),
    BLOCKED("Blocked")
}
