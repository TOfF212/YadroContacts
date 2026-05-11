package com.example.yadrocontacts.domain.entity

data class Contact(
    val id: Long,
    val phone: String,
    val name: String,
    val nickname: String,
    val email: String,
    val organization: String,
    val notes: String,
    val postalAddresses: String,
    val websites: String
)
