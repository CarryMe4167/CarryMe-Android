package com.carryme4167.carryme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_offer_new_ride.*

class OfferRide : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_new_ride)

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
        val ride = OfferRideObject(from, to, time, uid, seats)
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
}
