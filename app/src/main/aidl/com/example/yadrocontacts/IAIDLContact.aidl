package com.example.yadrocontacts;

import android.os.Parcel;
import android.os.Parcelable;

parcelable IAIDLContact {
    long id;
    String name;
    List<String> phones;
    String nickname;
    List<String> emails;
    String organization;
    String notes;
    List<String> postalAddresses;
    List<String> websites;
}