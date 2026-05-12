package com.example.yadrocontacts.data.repository

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.yadrocontacts.AIDLContactInterface
import com.example.yadrocontacts.DeleteRepeatContactsCallback
import com.example.yadrocontacts.GetContactCallback
import com.example.yadrocontacts.IAIDLContact
import com.example.yadrocontacts.data.mapper.toDomainContact
import com.example.yadrocontacts.data.service.AIDLContactService
import com.example.yadrocontacts.data.source.AIDLContactSource
import com.example.yadrocontacts.domain.entity.Contact
import com.example.yadrocontacts.domain.repository.ContactRepository
import com.example.yadrocontacts.domain.util.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val aidlContactService: AIDLContactSource
)
    : ContactRepository {


    override fun getContacts(): Flow<Result<List<Contact>>> = callbackFlow {
        delay(300)
        try {
            aidlContactService.getContacts(object : GetContactCallback.Stub() {
                override fun onSuccess(contacts: List<IAIDLContact>) {
                    trySend(Result.Success(contacts.map { it.toDomainContact() }))
                        .onFailure {
                            Log.d("ContactRepositoryImpl", "Error: ${it?.message}")
                        }
                }

                override fun onError(errorMessage: String?) {
                    trySend(Result.Error(Exception(errorMessage)))
                        .onFailure {
                            Log.d("ContactRepositoryImpl", "Error: ${it?.message}")
                        }
                }
            })
        } catch (e: Exception) {
            trySend(
                Result.Error(e)
            )
        }
        awaitClose {}


    }

    override fun deleteRepeatContact(): Flow<Result<String>> =
        callbackFlow {
            try {
                aidlContactService.deleteRepeatContacts(
                    callback = object : DeleteRepeatContactsCallback.Stub() {
                        override fun onSuccess(deletedContacts: Int) {
                            trySend(Result.Success(deletedContacts.toString()))
                                .onFailure {
                                    Log.d("ContactRepositoryImpl", "Error: ${it?.message}")
                                }
                        }

                        override fun onRepeatContactsNotFound() {
                            trySend(Result.Success("Repeat contacts not found"))
                                .onFailure {
                                    Log.d("ContactRepositoryImpl", "Error: ${it?.message}")
                                }
                        }

                        override fun onError(errorMessage: String?) {
                            trySend(Result.Error(Exception(errorMessage)))
                                .onFailure {
                                    Log.d("ContactRepositoryImpl", "Error: ${it?.message}")
                                }
                        }

                    }
                )
            }
            catch (e: Exception) {
                trySend(
                    Result.Error(e))

            }
            awaitClose {}
        }
    }