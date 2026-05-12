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
                    try {
                        val contacts = getContactList()
                        callback.onSuccess(contacts)
                    } catch (e: Exception){
                        Log.e("Service", "ERROR", e)

                        callback.onError(e.message?: "Error")
                    }

                }
            }

            override fun deleteRepeatContacts(callback: DeleteRepeatContactsCallback) {
                Log.d("Service", "deleteContacts")

                serviceScope.launch {
                    try{


                    val contacts = getContactList()
                    val groupedContacts = contacts.groupBy { it.toNormalizeContact() }
                    val repeatContacts = groupedContacts.values.filter { it.size > 1 }.flatMap { it.drop(1) }

                    if (repeatContacts.isEmpty()){
                        callback.onRepeatContactsNotFound()
                        return@launch
                    }

                    repeatContacts.forEach {

                        val deleted = contentResolver.delete(
                            ContactsContract.RawContacts.CONTENT_URI,
                            "${ContactsContract.RawContacts._ID} = ?",
                            arrayOf(it.id.toString())
                        )
                        Log.d("Service", "deleteContact ${it.name} $deleted")

                    }
                    callback.onSuccess(
                        repeatContacts.size
                    )
                    } catch (e: Exception){
                        Log.e("Service", "ERROR", e)
                        callback.onError(e.message?: "Error")
                    }
                }
            }


    }
}
    private fun getContactList(): List<IAIDLContact> {
        Log.d("Service", "getContactsList")

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
                Log.d("Service", "Get contact ${contact.id}")

            }
        }
        Log.d("Service", "ReturnContacts")

        return contacts
    }

    private fun getPhone(contact: IAIDLContact){
        Log.d("Service", "Phone}")
        val phones = mutableListOf<String>()

        contact.phones = emptyList<String>()

        val phoneCursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contact.id.toString()),
            null
        )
        phoneCursor?.use { pc ->
            while (pc.moveToNext()) {
                phones.add(
                    pc.getString(pc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))?:""
                )

            }
        }
        contact.phones =phones
    }
    private fun getEmails(contact: IAIDLContact){
        Log.d("Service", "Email}")
        val emails = mutableListOf<String>()
        contact.emails = emptyList<String>()

        val cursor  = contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
            arrayOf(contact.id.toString()),
            null
        )
        cursor?.use { c ->
            while (c.moveToNext()) {
                emails.add(
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS))?:""
                )

            }
        }
        contact.emails = emails
    }


    private fun getPostalAddresses(contact: IAIDLContact){
        Log.d("Service", "Address")
        val addresses = mutableListOf<String>()
        contact.postalAddresses = emptyList<String>()

        val cursor  = contentResolver.query(
            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ?",
            arrayOf(contact.id.toString()),
            null
        )
        cursor?.use { c ->
            while (c.moveToNext()) {
                addresses.add(
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS))?:""
                )

            }
        }
        contact.postalAddresses = addresses
    }
    private fun getWebSites(contact: IAIDLContact){
        Log.d("Service", "site")
        val sites = mutableListOf<String>()
        contact.websites = emptyList<String>()
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
                sites.add(
                    c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Website.URL))?:""
                )

            }
        }
        contact.websites = sites
    }

    private fun getNotes(contact: IAIDLContact){
        Log.d("Service", "note")
        contact.notes = ""
        val cursor  = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(
                contact.id.toString(),
                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE
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
        Log.d("Service", "nick")
        contact.nickname = ""
        val cursor  = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(
                contact.id.toString(),
                ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE
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
        Log.d("Service", "org")
        contact.organization = ""
        val cursor  = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(
                contact.id.toString(),
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
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