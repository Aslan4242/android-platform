package com.example.androidplatform.domain.models.cards

import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.domain.models.clients.Client
import com.example.androidplatform.domain.models.showcase.Product

data class Card (
    val id: Int,
    val account: Account,
    val client: Client,
    val cardProgram: String,
    val product: Product,
    val number: String,
    val month: String,
    val year: String,
    val state: String
)
