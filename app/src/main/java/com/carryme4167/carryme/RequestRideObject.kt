package com.carryme4167.carryme

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RequestRideObject(val from: String, val to: String,  val pickuptime: String, val passengerUID: String): Parcelable
{
    constructor() : this("", "", "",  "")
}