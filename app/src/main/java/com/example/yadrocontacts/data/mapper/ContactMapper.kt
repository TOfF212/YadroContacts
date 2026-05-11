package com.example.yadrocontacts.data.mapper

import com.example.yadrocontacts.IAIDLContact
import com.example.yadrocontacts.domain.entity.Contact

fun IAIDLContact.toDomainContact() = Contact(
    id = id,
    phone = phone,
    name = name,
    nickname = "",
    email = "",
    organization = "",
    notes = "",
    postalAddresses = "",
    websites = "",
)