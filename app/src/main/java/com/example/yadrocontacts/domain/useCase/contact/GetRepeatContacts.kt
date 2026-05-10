package com.example.yadrocontacts.domain.useCase.contact

import com.example.yadrocontacts.domain.repository.ContactRepository
import jakarta.inject.Inject

class GetRepeatContacts @Inject constructor(private val repository: ContactRepository){

    suspend operator fun invoke() = repository.getRepeatContacts()
}