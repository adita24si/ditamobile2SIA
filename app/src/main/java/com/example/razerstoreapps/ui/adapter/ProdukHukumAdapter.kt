package com.example.razerstoreapps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.razerstoreapps.data.model.ProdukHukum
import com.example.razerstoreapps.databinding.ItemProdukHukumBinding

class ProdukHukumAdapter(
    private val items: List<ProdukHukum>,
    private val onItemClick: (ProdukHukum) -> Unit
) : RecyclerView.Adapter<ProdukHukumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProdukHukumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemProdukHukumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProdukHukum) {
            binding.imgDocIcon.setImageResource(item.iconRes)
            binding.tvDocName.text = item.name
            binding.tvDocNumber.text = "No. ${item.number} Tahun ${item.year}"
            binding.tvDocDesc.text = item.shortDescription

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
