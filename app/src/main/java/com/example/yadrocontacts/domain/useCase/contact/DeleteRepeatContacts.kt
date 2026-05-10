package com.example.yadrocontacts.domain.useCase.contact

import com.example.yadrocontacts.domain.repository.ContactRepository
import jakarta.inject.Inject
import com.example.yadrocontacts.domain.util.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class DeleteRepeatContacts
@Inject constructor(private val repository: ContactRepository){

    suspend operator fun invoke(contactIds: List<String>) = flow {
        emit(Result.Loading)

        contactIds.forEach { contactId ->
            val result = repository.deleteContact(contactId).first {
                it !is Result.Loading
            }
            when (result) {
                is Result.Error -> {
                    emit(Result.Error(result.exception))
                    return@flow
                }
                else -> {}
            }
        }
        emit(Result.Success<Unit>(Unit))
    }

}