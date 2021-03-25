package com.arabam.android.assigment.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arabam.android.assigment.data.model.CarEntity
import com.arabam.android.assigment.databinding.ItemCarsBinding
import com.arabam.android.assigment.util.downloadImage

class CarListAdapter : PagingDataAdapter<CarEntity, CarListAdapter.CarViewHolder>(CAR_COMPARATOR) {

    var onItemSelectListener: ((Long?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding =
            ItemCarsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class CarViewHolder(private val binding: ItemCarsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(car: CarEntity) {
            binding.apply {

                itemView.setOnClickListener {
                    onItemSelectListener?.invoke(car.carId)
                }
                carImageView.downloadImage(
                    car.photo.replace("{0}", "800x600"),
                    car.photo.replace("{0}", "1920x1024")
                )
                carTitleTextView.text = car.title
                cityNameTextView.text = car.cityName
                townNameTextView.text = car.townName
                slashTextView.text = "/"
                priceTextView.text = car.priceFormatted
            }
        }
    }

    companion object {
        private val CAR_COMPARATOR = object : DiffUtil.ItemCallback<CarEntity>() {
            override fun areItemsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean =
                oldItem.carId == newItem.carId

            override fun areContentsTheSame(oldItem: CarEntity, newItem: CarEntity): Boolean =
                oldItem == newItem
        }
    }
}