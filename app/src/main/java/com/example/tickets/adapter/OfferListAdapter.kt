package com.example.tickets.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tickets.R
import com.example.tickets.databinding.ItemOfferBinding
import com.example.tickets.model.entity.Offer

class DiffUtilCallback : DiffUtil.ItemCallback<Offer>() {
    override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
        return oldItem == newItem
    }
}

class OfferListAdapter : ListAdapter<Offer, OfferListAdapter.ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e("qwerty", "onCreateViewHolder: ")
        return ViewHolder(
            ItemOfferBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("qwerty", "onBindViewHolder: ")
        holder.bind(currentList[position])
    }

    inner class ViewHolder(
        private val binding: ItemOfferBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(offer: Offer) {
            val flight = offer.flight

            with(binding) {
                departureTime.text = flight.departureTimeInfo
                arrivalTime.text = flight.arrivalTimeInfo
                route.text = context.getString(
                    R.string.route_fmt,
                    flight.departureLocation.code,
                    flight.arrivalLocation.code
                )
                duration.text = context.getString(
                    R.string.time_fmt,
                    getTimeFormat(flight.duration).first.toString(),
                    getTimeFormat(flight.duration).second.toString()
                )
                direct.text = context.getString(R.string.direct)
                price.text = context.getString(R.string.price_fmt, offer.price.toString())
                airlineImage
            }
            Glide
                .with(binding.root.context)
                .load(getImageUrl(offer.flight.airline.name))
                .into(binding.airlineImage);
        }

        private fun getImageUrl(company: String): String {
            return when (company) {
                "Air Astana" -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSfdDigK6frN528_PFobyIfXBSqiWrGwVya7Q&s"
                "FlyArystan" -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwvnQGCqt1wZ2_10Ge4sJ1VaqCCcLZX7jpqw&s"
                "SCAT Airlines" -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTR3jk_ySu9O5867a40euOrV4Vo8-7Mx22ouQ&s"
                "QazaqAir" -> "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSdd_EGn0rKMInrlF3Cbq846zAmjwuJ4GeQGA&s"
                else -> ""
            }
        }

        private fun getTimeFormat(minutes: Int): Pair<Int, Int> = Pair(
            first = minutes / 60,
            second = minutes % 60
        )

    }
}