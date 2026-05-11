// AIDLContactInterface.aidl
package com.example.yadrocontacts;
import com.example.yadrocontacts.GetContactCallback;
// Declare any non-default types here with import statements

interface AIDLContactInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void getContacts(GetContactCallback callback);
}