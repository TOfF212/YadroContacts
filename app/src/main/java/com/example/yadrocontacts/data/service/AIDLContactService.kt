package com.example.yadrocontacts.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.yadrocontacts.AIDLContactInterface
import com.example.yadrocontacts.GetContactCallback
import com.example.yadrocontacts.IAIDLContact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.random.Random

class AIDLContactService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override fun onBind(intent: Intent): IBinder {
        return object : AIDLContactInterface.Stub(){
            override fun getContacts(callback: GetContactCallback) {
                Log.d("Service", "getContacts")
                serviceScope.launch {

                    val cursor = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC"
                    )
                    val contacts = mutableListOf<IAIDLContact>()
                    cursor?.use {
                        while (it.moveToNext()) {
                            val contact = IAIDLContact()
                            contact.id = it.getLong(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                            contact.name = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))

                            val hasPhoneNumber = it.getInt(it.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            if (hasPhoneNumber > 0) {
                                val phoneCursor = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    arrayOf(contact.id.toString()),
                                    null
                                )

                                phoneCursor?.use { pc ->
                                    if (pc.moveToNext()){
                                        contact.phone = pc.getString(pc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                                    }
                                }
                                contacts.add(contact)

                            }

                        }
                    }
                    callback.onSuccess(contacts)
                }
                }
            }
    }
}
