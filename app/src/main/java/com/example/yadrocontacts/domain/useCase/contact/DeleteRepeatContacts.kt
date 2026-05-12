package com.example.yadrocontacts.domain.useCase.contact

import com.example.yadrocontacts.domain.repository.ContactRepository
import jakarta.inject.Inject
import com.example.yadrocontacts.domain.util.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class DeleteRepeatContacts
@Inject constructor(private val repository: ContactRepository){

     operator fun invoke(contactIds: List<String>) = repository.deleteRepeatContact()

}