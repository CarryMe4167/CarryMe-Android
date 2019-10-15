package com.carryme4167.carryme

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RequestRideObject(val from: String, val to: String,  val pickuptime: String, val passengerUID: String, val fromlat: Double, val fromlong: Double, val tolat: Double, val tolong: Double): Parcelable
{
    constructor() : this("", "", "",  "", 0.0, 0.0, 0.0, 0.0)
}