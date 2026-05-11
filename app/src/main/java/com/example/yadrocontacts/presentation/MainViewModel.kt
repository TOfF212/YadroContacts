package com.example.yadrocontacts.presentation

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _isPermissionGranted = MutableStateFlow(false)
    val isPermissionGranted = _isPermissionGranted.asStateFlow()

    private val _visiblePermissionDialogQueue = MutableStateFlow<List<String>>(emptyList())
    val visiblePermissionDialogQueue = _visiblePermissionDialogQueue.asStateFlow()

    val permissionsList = arrayOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS
    )



    init {
        checkPermissions()
    }

    private fun checkPermissions(){
        _isPermissionGranted.value = permissionsList.all{
            permission ->
            ContextCompat.checkSelfPermission(getApplication(context), permission) == PackageManager.PERMISSION_GRANTED
        }

    }
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