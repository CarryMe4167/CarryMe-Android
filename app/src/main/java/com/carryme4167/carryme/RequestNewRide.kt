package com.carryme4167.carryme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_request_new_ride.*

class RequestNewRide : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_new_ride)

        requestButton.setOnClickListener {
            val _from = from.text.toString()
            val _to = to.text.toString()
            val _pickuplocation = pickup.text.toString()
            val _pickuptime = time.text.toString()
            val _uid = FirebaseAuth.getInstance().uid.toString()
            offerRide(_from, _to, _pickuplocation, _pickuptime, _uid)
        }
    }

    fun offerRide(from: String, to: String, pickuplocation: String, pickuptime: String, uid: String)
    {
        val dbref = FirebaseFirestore.getInstance().collection("ridesRequested")
        val ride = RequestRideObject(from, to, pickuplocation, pickuptime, uid)
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
}
