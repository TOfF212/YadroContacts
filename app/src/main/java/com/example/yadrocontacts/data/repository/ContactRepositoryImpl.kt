package com.example.yadrocontacts.data.repository

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.yadrocontacts.AIDLContactInterface
import com.example.yadrocontacts.GetContactCallback
import com.example.yadrocontacts.IAIDLContact
import com.example.yadrocontacts.data.service.AIDLContactService
import com.example.yadrocontacts.data.source.AIDLContactSource
import com.example.yadrocontacts.domain.entity.Contact
import com.example.yadrocontacts.domain.repository.ContactRepository
import com.example.yadrocontacts.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val aidlContactService: AIDLContactSource
)
    : ContactRepository {


    override fun getContacts(): Flow<Result<List<Contact>>>  = callbackFlow {
        aidlContactService.getContacts(object : GetContactCallback {
            override fun onSuccess(contacts: List<IAIDLContact?>?) {
                TODO("Not yet implemented")
            }

            override fun asBinder(): IBinder? {
                TODO("Not yet implemented")
            }
        })
    }

    override fun getRepeatContacts(): Flow<Result<List<Contact>>> {
        TODO("Not yet implemented")
    }

    override fun deleteContact(contactId: String): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }
//    private fun changeColor(){
//        AIDLColorService?.getColor (
//            object : AIDLColorCallback.Stub() {
//                override fun onColorGenerated(color: Int) {
//                    runOnUiThread {
//                        colorButton.value = Color(color)
//                    }
//                }
//            }
//        )
//    }
}