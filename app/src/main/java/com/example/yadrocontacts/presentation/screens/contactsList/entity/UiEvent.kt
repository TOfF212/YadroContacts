package com.example.yadrocontacts.presentation.screens.contactsList.entity

sealed class UiEvent {
    data class ShowMessage(
        val message: String,
    ): UiEvent()
}