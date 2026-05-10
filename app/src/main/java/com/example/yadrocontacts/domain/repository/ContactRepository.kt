package com.example.yadrocontacts.domain.repository

import com.example.yadrocontacts.domain.entity.Contact
import kotlinx.coroutines.flow.Flow
import com.example.yadrocontacts.domain.util.Result

interface ContactRepository {
     fun getContacts(): Flow<Result<List<Contact>>>
     fun getRepeatContacts(): Flow<Result<List<Contact>>>
     fun deleteContact(contactId: String): Flow<Result<Unit>>
}