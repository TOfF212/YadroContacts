package com.example.yadrocontacts.domain.entity

data class Contact(
    val id: Long,
    val nickname: String?,
    val name: String?,
    val phones: List<String>,
    val emails: List<String>,
    val photoUri: String?,
    val relations: List<String>,
    val events: List<String>,
    val notes: List<String>,
    val postalAddresses: List<String>,
    val websites: List<String>
)
