package com.carryme4167.carryme


import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.cardview_rides_fetched.view.*

class RidesFetchedAdapter (val fetchedRide: OfferRideObject): Item<GroupieViewHolder>()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.rideQuickInfo.setText("From: ${fetchedRide.from} To: ${fetchedRide.to}")
        viewHolder.itemView.time.setText("Time: ${fetchedRide.time}")
        viewHolder.itemView.seats.setText("For (seats): ${fetchedRide.seats}")
    }

    override fun getLayout(): Int {
        return R.layout.cardview_rides_fetched
    }
}