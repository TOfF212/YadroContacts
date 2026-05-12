package com.example.yadrocontacts.data.service

import com.example.yadrocontacts.IAIDLContact

data class NormalizeContact (
    val phones: List<String>,
    val name: String,
    val nickname: String,
    val emails: List<String>,
    val organization: String,
    val notes: String,
    val postalAddresses: List<String>,
    val websites: List<String>
)

fun IAIDLContact.toNormalizeContact() = NormalizeContact(
    phones = phones.map{
        it.filter { it.isDigit() }
    }
        .sorted(),
    name = name.lowercase().trim(),
    nickname = nickname.lowercase().trim(),
    emails = emails.map{
        it.lowercase().trim()
    }.sorted(),
    organization = organization.lowercase().trim(),
    notes = notes.lowercase().trim(),
    postalAddresses = postalAddresses.map{
        it.lowercase().trim()
    }.sorted(),
    websites = websites.map{
        it.lowercase().trim()
    }.sorted()
)