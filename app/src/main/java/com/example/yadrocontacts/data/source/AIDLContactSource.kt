package com.example.yadrocontacts.data.source

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.example.yadrocontacts.AIDLContactInterface
import com.example.yadrocontacts.GetContactCallback
import com.example.yadrocontacts.IAIDLContact
import com.example.yadrocontacts.data.service.AIDLContactService
import com.example.yadrocontacts.domain.entity.Contact
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.apply

@Singleton
class AIDLContactSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var aIDLContactService: AIDLContactInterface? = null


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            aIDLContactService = AIDLContactInterface.Stub.asInterface(service)
            Log.d("AIDLContactSource", "serviceConnection")

        }
        override fun onServiceDisconnected(name: ComponentName?) {
            aIDLContactService = null
        }
    }
    init {
        bind()
    }

    fun bind(){


        val intent = Intent(context, AIDLContactService::class.java)
        context.bindService(
            intent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        Log.d("AIDLContactSource", "bind")


    }

    fun unbind(){

        context.unbindService(serviceConnection)
    }

    fun getContacts(callback: GetContactCallback){
        Log.d("AIDLContactSource", "getContacts")
         aIDLContactService?.getContacts(callback)
    }

}