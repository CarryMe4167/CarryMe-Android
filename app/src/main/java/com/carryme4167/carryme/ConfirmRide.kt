package com.carryme4167.carryme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_confirm_ride.*

class ConfirmRide : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_ride)

        val ride_item_temp = intent.getParcelableExtra<OfferRideObject>("Ride")
        Toast.makeText(this, "${ride_item_temp.from.toString()} to ${ride_item_temp.to.toString()} at ${ride_item_temp.time.toString()} for ${Integer.parseInt(ride_item_temp.seats.toString())}", Toast.LENGTH_SHORT).show()

        from.setText(ride_item_temp.from.toString())
        to.setText(ride_item_temp.to.toString())
        time.setText(ride_item_temp.time.toString())
        seats.setText(ride_item_temp.seats.toString())

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
