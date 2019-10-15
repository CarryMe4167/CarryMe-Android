package com.carryme4167.carryme


import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.cardview_rides_requested.view.*

class RidesRequestedAdapter (val requestedRide: RequestRideObject): Item<GroupieViewHolder>()
{
    override fun getLayout(): Int {
        return R.layout.cardview_rides_requested
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.rideQuickInfo.setText("From: ${requestedRide.from} To: ${requestedRide.to}")
        viewHolder.itemView.pickuptime.setText("Pickup time: ${requestedRide.pickuptime}")
    }
}