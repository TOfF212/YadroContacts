package com.example.yadrocontacts.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.yadrocontacts.AIDLContactInterface
import com.example.yadrocontacts.DeleteRepeatContactsCallback
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
        return object : AIDLContactInterface.Stub() {

            override fun getContacts(callback: GetContactCallback) {
                Log.d("Service", "getContacts")
                serviceScope.launch {
                    val contacts = getContactList()
                    callback.onSuccess(contacts)
                }
            }

            override fun deleteRepeatContactsCallback(callback: DeleteRepeatContactsCallback) {
            }


    }
}
    private fun getContactList(): List<IAIDLContact> {

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        val contacts = mutableListOf<IAIDLContact>()
        cursor?.use {
            while (it.moveToNext()) {
                val contact = IAIDLContact()
                contact.id =
                    it.getLong(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                contact.name =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))

                getPhone(contact)

                getEmails(contact)

                getPostalAddresses(contact)

                getNotes(contact)

                getNickname(contact)

                getOrganization(contact)

                getWebSites(contact)

                contacts.add(contact)

            }
        }
        return contacts
    }

    private fun getPhone(contact: IAIDLContact){
        val phoneCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contact.id.toString()),
            null
        )
        phoneCursor?.use { pc ->
            while (pc.moveToNext()) {
                contact.phones.add(
                    pc.getString(pc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))?:""
                )

            }
        }
    }
    private fun getEmails(contact: IAIDLContact){
        val cursor  = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
            arrayOf(contact.id.toString()),
            null
        )
        cursor?.use { c ->
            while (c.moveToNext()) {
                contact.emails.add(
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS))?:""
                )

            }
        }
    }


    private fun getPostalAddresses(contact: IAIDLContact){
        val cursor  = contentResolver.query(
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
            arrayOf(contact.id.toString()),
            null
        )
        cursor?.use { c ->
            while (c.moveToNext()) {
                contact.postalAddresses.add(
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS))?:""
                )

            }
        }
    }
    private fun getWebSites(contact: IAIDLContact){
        val cursor  = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(
                contact.id.toString(),
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
            ),
            null
        )
        cursor?.use { c ->
            while (c.moveToNext()) {
                contact.emails.add(
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS))?:""
                )

            }
        }
    }

    private fun getNotes(contact: IAIDLContact){
        val cursor  = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(
                contact.id.toString(),
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
            ),
            null
        )

        cursor?.use{
            if (it.moveToFirst()) {

                contact.notes  =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Note.NOTE))?:""
            }

        }
    }

    private fun getNickname(contact: IAIDLContact){
        val cursor  = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(
                contact.id.toString(),
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
            ),
            null
        )

        cursor?.use{
            if (it.moveToFirst()) {

                contact.nickname =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Nickname.NAME))?:""


            }

        }
    }
    private fun getOrganization(contact: IAIDLContact){
        val cursor  = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(
                contact.id.toString(),
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
            ),
            null
        )

        cursor?.use{
            if (it.moveToFirst()) {


                contact.organization  =
                    it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Organization.COMPANY))?:""
            }

        }
    }
}