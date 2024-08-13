package com.example.androidplatform.domain.models.account

import com.example.androidplatform.domain.models.clients.Client

data class Account(
    val id: Long,
    val client: Client?,
    val createdDate: String,
    val currency: Short,
    val number: String,
    val name: String,
    val balance: Long,
    val state: String
)
