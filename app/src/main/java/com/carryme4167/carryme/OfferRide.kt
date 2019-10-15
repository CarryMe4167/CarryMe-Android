package com.carryme4167.carryme

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_offer_new_ride.*
import java.io.IOException

class OfferRide : AppCompatActivity(), OnMapReadyCallback{

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    companion object
    {
        private const val FROM_CODE = 1
        private const val TO_CODE = 2
    }

    private lateinit var mMap: GoogleMap
    private var fromlatD: Double = 0.0
    private var fromlongD: Double = 0.0
    private var tolatD: Double = 0.0
    private var tolongD: Double = 0.0

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ( requestCode == FROM_CODE)
        {
            if ( resultCode == Activity.RESULT_OK)
            {
                val lat = data?.getDoubleExtra("locdatalat", 0.0)
                val long = data?.getDoubleExtra("locdatalong", 0.0)
                fromlatD = lat!!
                fromlongD = long!!
                Log.d("TEST", "Received $lat, $long")
                val address = getAddress(lat, long)
                Log.d("TEST", "Address = $address")
                from.setText(address)
                val fromMarker = LatLng(lat!!, long!!)
                mMap.addMarker(MarkerOptions().position(fromMarker).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromMarker, 15f))
            }
        }
        else if ( requestCode == TO_CODE)
        {
            if ( resultCode == Activity.RESULT_OK)
            {
                val lat = data?.getDoubleExtra("locdatalat", 0.0)
                val long = data?.getDoubleExtra("locdatalong", 0.0)
                tolatD = lat!!
                tolongD = long!!
                Log.d("TEST", "Received $lat, $long")
                val address = getAddress(lat, long)
                Log.d("TEST", "Address = $address")
                to.setText(address)
                val toMarker = LatLng(lat!!, long!!)
                mMap.addMarker(MarkerOptions().position(toMarker).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toMarker, 15f))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_new_ride)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.overallMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fromButton.setOnClickListener {
            val intent = Intent(this, SupportMap::class.java)
            startActivityForResult(intent, FROM_CODE)
        }
//
        toButton.setOnClickListener {
            val intent = Intent(this, SupportMap::class.java)
            startActivityForResult(intent, TO_CODE)
        }

        offerButton.setOnClickListener {
            val _from = from.text.toString()
            val _to = to.text.toString()
            val _time = time.text.toString()
            val _seats = Integer.parseInt(seats.text.toString())
            val _uid = FirebaseAuth.getInstance().uid.toString()
            offerRide(_from, _to, _time, _uid, _seats)
        }
    }

    fun offerRide(from: String, to: String, time: String, uid: String, seats: Int)
    {
        val dbref = FirebaseFirestore.getInstance().collection("ridesOffered")
        val ride = OfferRideObject(from, to, time, uid, seats, fromlatD, fromlongD, tolatD, tolongD)
        dbref.document("$from $to $time").set(ride)
            .addOnSuccessListener {
                Toast.makeText(this, "Ride successfully pushed to database", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Driver::class.java)
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
                addressText += address.getAddressLine(0)
            }
        } catch (e: IOException) {
            Log.d( "TEST", e.localizedMessage)
        }

        return addressText
    }

}
