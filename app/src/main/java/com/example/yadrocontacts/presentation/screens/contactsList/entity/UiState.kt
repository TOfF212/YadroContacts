package com.example.yadrocontacts.presentation.screens.contactsList.entity

sealed class UiState {
    object Loading : UiState()
    data class Success(val contacts: List<PresentationContact>) : UiState()
    object Error: UiState()
}