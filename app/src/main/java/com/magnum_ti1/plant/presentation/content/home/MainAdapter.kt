package com.magnum_ti1.plant.presentation.content.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.magnum_ti1.plant.data.entity.Plant
import com.magnum_ti1.plant.databinding.ItemRowPlantBinding

class MainAdapter(private val clickListener: (Int?) -> Unit) :
    ListAdapter<Plant, MainAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemRowPlantBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: Plant, clickListener: (Int?) -> Unit) {
            with(binding) {
                tvPlantName.text = plant.name
            }
            itemView.setOnClickListener {
                clickListener(plant.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRowPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = getItem(position) as Plant
        holder.bind(plant, clickListener)
    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Plant>() {
            override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean =
                oldItem == newItem
        }
    }
}