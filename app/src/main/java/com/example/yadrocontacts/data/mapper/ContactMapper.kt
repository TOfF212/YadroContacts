package com.example.yadrocontacts.data.mapper

import com.example.yadrocontacts.IAIDLContact
import com.example.yadrocontacts.domain.entity.Contact

fun IAIDLContact.toDomainContact() = Contact(
    id = id,
    phones = phones,
    name = name,
    nickname = nickname,
    emails = emails,
    organization = organization,
    notes = notes,
    postalAddresses = postalAddresses,
    websites =  websites,
)