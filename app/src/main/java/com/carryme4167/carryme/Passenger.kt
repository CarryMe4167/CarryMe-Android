package com.carryme4167.carryme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_passenger.*

class Passenger : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger)

        //Google Maps stuff
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.passengerMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginRegister::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        newRideRequestButton.setOnClickListener {
            val intent = Intent(this, RequestNewRide::class.java)
            startActivity(intent)
        }

        fetchrides()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    fun fetchrides()
    {
        val dbref = FirebaseFirestore.getInstance().collection("ridesOffered")
        dbref.addSnapshotListener { snap, e ->
            val adapter = GroupAdapter<GroupieViewHolder>()
            if ( e != null )
            {
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
            else
            {
                for ( doc in snap!! )
                {
                    Log.d("Recyclerview", "Found object")
                    val temp = doc.toObject(OfferRideObject::class.java)
                    if ( temp != null )
                    {
                        Log.d("Recyclerview", "Found object ${temp.from} ${temp.to} ${temp.time}")
                        adapter.add(RidesFetchedAdapter(temp))
                    }
                }
                availableRidesRecycler.adapter = adapter

                adapter.setOnItemClickListener{ item, view ->
                    val item_item = item as RidesFetchedAdapter
                    val intent = Intent(view.context, ConfirmRide::class.java)
                    intent.putExtra("Ride", item_item.fetchedRide)
                    startActivity(intent)
                }
            }
        }
    }
}
