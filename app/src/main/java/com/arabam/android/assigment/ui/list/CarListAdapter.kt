package com.arabam.android.assigment.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arabam.android.assigment.data.model.Car
import com.arabam.android.assigment.databinding.ItemCarsBinding
import com.arabam.android.assigment.util.downloadImage

class CarListAdapter : PagingDataAdapter<Car, CarListAdapter.CarViewHolder>(CAR_COMPARATOR) {

    var onItemSelectListener: ((Car?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding =
            ItemCarsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) holder.bind(currentItem)
    }

    inner class CarViewHolder(private val binding: ItemCarsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car) {
            binding.apply {

                itemView.setOnClickListener {
                    onItemSelectListener?.invoke(car)
                }
                carImageView.downloadImage(
                    car.photo.replace("{0}", "800x600"),
                    car.photo.replace("{0}", "1920x1024")
                )
                carTitleTextView.text = car.title
                cityNameTextView.text = car.location.cityName
                townNameTextView.text = car.location.townName
                slashTextView.text = "/"
                priceTextView.text = car.priceFormatted
            }
        }
    }

    companion object {
        private val CAR_COMPARATOR = object : DiffUtil.ItemCallback<Car>() {
            override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean =
                oldItem == newItem
        }
    }
}