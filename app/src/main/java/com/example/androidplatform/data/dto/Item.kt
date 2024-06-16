package com.example.androidplatform.data.dto

import com.example.androidplatform.domain.models.clients.ClientsItem

data class Item(
    val login: String,
    val email: String,
    val lastName: String,
    val firstName: String,
    val middleName: String,
    val sex: String,
    val birthdate: String,
    val phoneNumber: String,
    val address: String
)

fun Item.mapToClientItem(): ClientsItem {
    return ClientsItem(
        login = this.login,
        email = this.email,
        lastName = this.lastName,
        firstName = this.firstName,
        middleName = this.middleName,
        sex = this.sex,
        birthdate = this.birthdate,
        phoneNumber = this.phoneNumber,
        address = this.address
    )
}
