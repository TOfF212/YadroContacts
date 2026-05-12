package com.example.yadrocontacts.domain.entity

data class Contact(
    val id: Long,
    val phones: List<String>,
    val name: String,
    val nickname: String,
    val emails: List<String>,
    val organization: String,
    val notes: String,
    val postalAddresses: List<String>,
    val websites: List<String>
)
