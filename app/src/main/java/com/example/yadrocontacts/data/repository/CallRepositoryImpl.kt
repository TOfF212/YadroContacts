package com.example.yadrocontacts.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.yadrocontacts.domain.repository.CallRepository
import com.example.yadrocontacts.presentation.screens.contactsList.entity.PresentationContact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CallRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): CallRepository {



    override fun CreateCall(tel: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$tel")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.data = Uri.parse("tel:$tel")
        context.startActivity(intent)
    }
}