package com.example.yadrocontacts.domain.useCase.call

import com.example.yadrocontacts.domain.repository.CallRepository
import com.example.yadrocontacts.domain.repository.ContactRepository
import jakarta.inject.Inject


class CreateCall
@Inject constructor(private val repository: CallRepository){

    operator fun invoke(tel: String) = repository.CreateCall(tel)

}