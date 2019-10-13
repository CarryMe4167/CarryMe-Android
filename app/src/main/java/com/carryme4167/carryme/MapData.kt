package com.carryme4167.carryme

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class MapData(val lat: Double, val long: Double): Parcelable {
    constructor():this(0.0, 0.0)
}