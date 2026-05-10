package com.example.yadrocontacts.presentation.screens.contactsList.entity

sealed class UiEvent {
    data class ShowError(
        val message: String,
    ) : UiEvent()
}