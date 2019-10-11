package com.carryme4167.carryme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class ConfirmRide : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_ride)

        val ride_item_temp = intent.getParcelableExtra<OfferRideObject>("Ride")
        Toast.makeText(this, "${ride_item_temp.from.toString()} to ${ride_item_temp.to.toString()} at ${ride_item_temp.time.toString()} for ${Integer.parseInt(ride_item_temp.seats.toString())}", Toast.LENGTH_SHORT).show()
    }
}
