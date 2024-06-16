package com.example.androidplatform.data.network

import com.example.androidplatform.data.models.PersonalData

class PersonalDataResponse(
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
