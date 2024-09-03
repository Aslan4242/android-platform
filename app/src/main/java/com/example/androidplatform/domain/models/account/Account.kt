package com.example.androidplatform.domain.models.account

import com.example.androidplatform.domain.models.clients.Client

data class Account(
    val id: Int,
    val client: Client?,
    val createdDate: String,
    val currency: Int,
    val number: String,
    val name: String,
    val balance: Long,
    val state: String
)
