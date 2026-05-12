package com.example.yadrocontacts.presentation.screens.contactsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yadrocontacts.domain.useCase.contact.DeleteRepeatContacts
import com.example.yadrocontacts.domain.useCase.contact.GetContacts
import com.example.yadrocontacts.presentation.screens.contactsList.entity.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.example.yadrocontacts.domain.util.Result
import com.example.yadrocontacts.presentation.screens.contactsList.entity.UiEvent
import com.example.yadrocontacts.presentation.util.toPresentationContact
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val deleteContactsUseCase: DeleteRepeatContacts,
    private val getContactsUseCase: GetContacts
): ViewModel() {

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    private val refreshTrigger  = MutableStateFlow(0)

    val ui: StateFlow<UiState> = refreshTrigger.flatMapLatest {
        getContactsUseCase()
    }
        .map {
        result ->
        when(result){

            is Result.Success -> {
                UiState.Success(result.data.map { it.toPresentationContact() })
            }

            is Result.Error -> {
                UiEvent.ShowMessage(result.exception.message ?: "Unknown error")
                UiState.Error
            }

            is Result.Loading -> {
                UiState.Loading
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    fun deleteRepeatContacts(){
        viewModelScope.launch {
            deleteContactsUseCase().collect{
                result ->
                when(result){
                    is Result.Success -> {
                        refreshTrigger.value++

                        _event.emit(UiEvent.ShowMessage(result.data))
                    }
                    is Result.Error -> {
                        _event.emit(UiEvent.ShowMessage(result.exception.message ?: "Unknown error"))
                    }
                    is Result.Loading -> {

                    }
                }
            }
        }
    }

}