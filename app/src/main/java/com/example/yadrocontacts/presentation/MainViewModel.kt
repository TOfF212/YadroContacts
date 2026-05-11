package com.example.yadrocontacts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _isPermissionGranted = MutableStateFlow(false)
    val isPermissionGranted = _isPermissionGranted.asStateFlow()

    private val _visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())
    val visiblePermissionDialogQueue = _visiblePermissionDialogQueue.asStateFlow()

    fun onPermissionResult(permission:  Map<String,  Boolean>) {
        viewModelScope.launch {
            if (permission.all { it.value == true }) {
                _isPermissionGranted.emit(true)

            } else {
                permission.forEach { perm, isGranted ->
                    if (!isGranted) {
                        _visiblePermissionDialogQueue.value =
                            visiblePermissionDialogQueue.value + perm
                    }
                }
            }

        }
    }
    fun dismissDialog(){
        _visiblePermissionDialogQueue.value = visiblePermissionDialogQueue.value.drop(1)
    }
}