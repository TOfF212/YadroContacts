package com.example.yadrocontacts.presentation.util

import com.example.yadrocontacts.domain.entity.Contact
import com.example.yadrocontacts.presentation.screens.contactsList.entity.PresentationContact

fun Contact.toPresentationContact() = PresentationContact(
    id = id,
    name = name?:"Нет имени",
    phone = phones.joinToString()
)