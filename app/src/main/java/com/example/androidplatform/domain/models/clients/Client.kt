package com.example.androidplatform.domain.models.clients

data class Client (
    val login: String,
    val email: String,
    val lastName: String,
    val firstName: String,
    val middleName: String,
    val sex: String,
    val birthdate: String,
    val phoneNumber: String,
    val address: String,
    val isMustChangePassword: Boolean
)
