// GetContactCallback.aidl
package com.example.yadrocontacts;


import com.example.yadrocontacts.IAIDLContact;
//import com.example.yadrocontacts.AIDLContact;
// Declare any non-default types here with import statements

interface GetContactCallback {

    void onSuccess(in List<IAIDLContact> contacts);
}