package com.carryme4167.carryme

import com.google.type.LatLng

class LocationData (val uid: String, val lat: Double, val long: Double, val name: String)
{
    constructor():this("", 0.0, 0.0, "")
}