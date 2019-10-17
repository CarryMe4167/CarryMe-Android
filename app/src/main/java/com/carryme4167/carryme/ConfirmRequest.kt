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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_confirm_request.*
import java.util.ArrayList
import android.os.AsyncTask.execute
import android.R.attr.end
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.directions.route.*
import android.os.AsyncTask.execute
import android.R.attr.end
import com.directions.route.Routing
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.maps.model.*


class ConfirmRequest : AppCompatActivity(), OnMapReadyCallback, RoutingListener {

    override fun onRoutingSuccess(p0: ArrayList<Route>?, p1: Int) {
        if ( polylines!!.size > 0 )
        {
            for (poly in polylines!!) {
                poly.remove()
            }
        }

        var polylines: ArrayList<Polyline>? = null

        for (i in 0 until p0!!.size) {

            //In case of more than 5 alternative routes
            val colorIndex = i % COLORS.size

            val polyOptions = PolylineOptions()
            polyOptions.color(resources.getColor(COLORS[colorIndex]))
            polyOptions.width((10 + i * 3).toFloat())
            polyOptions.addAll(p0.get(i).getPoints())
            val polyline = mMap.addPolyline(polyOptions)
            polylines!!.add(polyline)

            Toast.makeText(
                    applicationContext,
            "Route " + (i + 1) + ": distance - " + p0.get(i).getDistanceValue() + ": duration - " + p0.get(
                i
            ).getDurationValue(),
            Toast.LENGTH_SHORT
            ).show()
        }

    }

    private var polylines: List<Polyline>? = null
    private val COLORS = intArrayOf(
        R.color.dark_orange
    )

    override fun onRoutingCancelled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRoutingStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRoutingFailure(p0: RouteException?) {
        if(p0 != null) {
            Toast.makeText(this, "Error: " + p0.message, Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_request)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.confirmMapReq) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val ride_item_temp = intent.getParcelableExtra<RequestRideObject>("Ride")
//        Toast.makeText(this, "${ride_item_temp.from.toString()} to ${ride_item_temp.to.toString()} at ${ride_item_temp.pickuptime.toString()} for ${ride_item_temp.pickuplocation.toString()}", Toast.LENGTH_SHORT).show()

        markFrom.setOnClickListener {
            val fromMarker = LatLng(ride_item_temp.fromlat, ride_item_temp.fromlong)
            mMap.addMarker(
                MarkerOptions().position(fromMarker).icon(
                    BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_GREEN
                    )
                )
            )
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

        showRoute.setOnClickListener {
            drawRoute(ride_item_temp.fromlat, ride_item_temp.fromlong, ride_item_temp.tolat, ride_item_temp.tolong)
        }

        val fromLoc = Location("")
        fromLoc.latitude = ride_item_temp.fromlat
        fromLoc.longitude = ride_item_temp.fromlong

        val toLoc = Location("")
        toLoc.latitude = ride_item_temp.tolat
        toLoc.longitude = ride_item_temp.tolong

        val ridecost = fromLoc.distanceTo(toLoc) * 0.02

        from.setText("From: ${ride_item_temp.from.toString()}")
        to.setText("To: ${ride_item_temp.to.toString()}")
        pickuptime.setText("Pickup time: ${ride_item_temp.pickuptime.toString()}")
        cost.setText("Fare: BDT ${ridecost.toString()}")

        confirmContact.setOnClickListener {
            getDetails(ride_item_temp.passengerUID, ride_item_temp.from, ride_item_temp.to)
        }
    }

    private fun drawRoute(fromlat: Double, fromlong: Double, tolat: Double, tolong: Double) {
        val routing = Routing.Builder()
            .key("AIzaSyDRzz6Ux47lbdHbCwS0xa79gagHXa_PeCw")
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(this)
            .waypoints(LatLng(fromlat, fromlong), LatLng(tolat, tolong))
            .build()
        routing.execute()
    }

    fun getDetails(uid: String, from: String, to: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("Passenger").document("$uid")
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
