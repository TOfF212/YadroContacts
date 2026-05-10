package com.example.yadrocontacts.domain.repository

import com.example.yadrocontacts.domain.entity.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun getContacts(): Flow<Result<List<Contact>>>
    suspend fun getRepeatContacts(): Flow<Result<List<Contact>>>
    suspend fun deleteContact(contact: Contact): Flow<Result<Unit>>
}