package com.carryme4167.carryme

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_confirm_ride.*

class ConfirmRide : AppCompatActivity(), OnMapReadyCallback {

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        val fromMarker = LatLng(23.949032080238702, 90.38094911724329)
//            mMap.addMarker(
//                MarkerOptions().position(fromMarker).icon(
//                    BitmapDescriptorFactory.defaultMarker(
//                        BitmapDescriptorFactory.HUE_GREEN
//                    )
//                )
//            )
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromMarker, 15f))
//
//
//        val toMarker = LatLng(23.929739297197735, 90.38922540843487)
//        mMap.addMarker(MarkerOptions().position(toMarker).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toMarker, 15f))
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_ride)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.confirmMapRide) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val ride_item_temp = intent.getParcelableExtra<OfferRideObject>("Ride")
        Toast.makeText(this, "${ride_item_temp.from.toString()} to ${ride_item_temp.to.toString()} at ${ride_item_temp.time.toString()} for ${Integer.parseInt(ride_item_temp.seats.toString())}", Toast.LENGTH_SHORT).show()

        markFrom.setOnClickListener {
            val fromMarker = LatLng(ride_item_temp.fromlat, ride_item_temp.fromlong)
            mMap.addMarker(
                MarkerOptions().position(fromMarker).icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                )
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromMarker, 15f))
        }

        markTo.setOnClickListener {
            val toMarker = LatLng(ride_item_temp.tolat, ride_item_temp.tolong)
            mMap.addMarker(
                MarkerOptions().position(toMarker).icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_YELLOW
                    )
                )
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toMarker, 15f))
        }

        val fromLoc = Location("")
        fromLoc.latitude = ride_item_temp.fromlat
        fromLoc.longitude = ride_item_temp.fromlong

        val toLoc = Location("")
        toLoc.latitude = ride_item_temp.tolat
        toLoc.longitude = ride_item_temp.tolong

        val ridecost = fromLoc.distanceTo(toLoc) * 0.5

        from.setText(ride_item_temp.from.toString())
        to.setText(ride_item_temp.to.toString())
        time.setText(ride_item_temp.time.toString())
        seats.setText(ride_item_temp.seats.toString())
        cost.setText(ridecost.toString())

        confirmContact.setOnClickListener {
            getDetails(ride_item_temp.driverUID, ride_item_temp.from, ride_item_temp.to)
        }
    }

    fun getDetails(uid: String, from: String, to: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("Driver").document("$uid")
        dbref.get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                val dialog = AlertDialog.Builder(this)
                val dialogview = layoutInflater.inflate(R.layout.contact_dialog, null)
                dialogview.findViewById<TextView>(R.id.from).setText(from.toString())
                dialogview.findViewById<TextView>(R.id.to).setText(to.toString())
                dialogview.findViewById<TextView>(R.id.name).setText(user!!.username.toString())
                dialogview.findViewById<TextView>(R.id.contact).setText(user!!.phone.toString())

                dialog.setView(dialogview)
                dialog.setCancelable(true)
                val contactdialog = dialog.create()
                contactdialog.show()
            }
    }
}
