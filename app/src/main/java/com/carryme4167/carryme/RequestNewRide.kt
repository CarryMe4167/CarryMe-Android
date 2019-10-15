package com.carryme4167.carryme

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_request_new_ride.*
import java.io.IOException

class RequestNewRide : AppCompatActivity() {

    companion object
    {
        private const val FROM_CODE = 1
        private const val TO_CODE = 2
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( requestCode == FROM_CODE)
        {
            if ( resultCode == Activity.RESULT_OK)
            {
                val lat = data?.getDoubleExtra("locdatalat", 0.0)
                val long = data?.getDoubleExtra("locdatalong", 0.0)
                Log.d("TEST", "Received $lat, $long")
                val address = getAddress(lat, long)
                Log.d("TEST", "Address = $address")
                from.setText(address)
            }
        }
        else if ( requestCode == TO_CODE)
        {
            if ( resultCode == Activity.RESULT_OK)
            {
                val lat = data?.getDoubleExtra("locdatalat", 0.0)
                val long = data?.getDoubleExtra("locdatalong", 0.0)
                Log.d("TEST", "Received $lat, $long")
                val address = getAddress(lat, long)
                Log.d("TEST", "Address = $address")
                to.setText(address)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_new_ride)

        fromButton.setOnClickListener {
            val intent = Intent(this, SupportMap::class.java)
            startActivityForResult(intent, FROM_CODE)
        }
//
        toButton.setOnClickListener {
            val intent = Intent(this, SupportMap::class.java)
            startActivityForResult(intent, TO_CODE)
        }

        requestButton.setOnClickListener {
            val _from = from.text.toString()
            val _to = to.text.toString()
            val _pickuptime = pickuptime.text.toString()
            val _uid = FirebaseAuth.getInstance().uid.toString()
            requestRide(_from, _to,  _pickuptime, _uid)
        }
    }

    fun requestRide(from: String, to: String,  pickuptime: String, uid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("ridesRequested")
        val ride = RequestRideObject(from, to,  pickuptime, uid)
        dbref.document("$from $to $pickuptime").set(ride)
            .addOnSuccessListener {
                Toast.makeText(this, "Ride successfully pushed to database", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Passenger::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun getAddress(lat: Double?, long: Double?): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(lat!!, long!!, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                Log.d("TEST", "Addresses is not null")
                address = addresses[0]
                Log.d("TEST", "$address")
//                for (i in 0 until address.maxAddressLineIndex) {
//                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
//                    Log.d("TEST", "$addressText")
//                }
                addressText += address.getAddressLine(0)
            }
        } catch (e: IOException) {
            Log.d( "TEST", e.localizedMessage)
        }

        return addressText
    }
}
