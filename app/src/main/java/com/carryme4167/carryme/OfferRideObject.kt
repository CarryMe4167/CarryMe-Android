package com.carryme4167.carryme

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class OfferRideObject (val from: String, val to: String, val time: String, val driverUID: String, val seats: Int): Parcelable
{
    constructor() : this("", "", "", "", 0)
}