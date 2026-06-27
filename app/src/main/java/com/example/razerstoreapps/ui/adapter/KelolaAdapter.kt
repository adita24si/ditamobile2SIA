package com.example.razerstoreapps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.razerstoreapps.R
import com.example.razerstoreapps.data.database.ProdukHukumEntity
import com.example.razerstoreapps.databinding.ItemKelolaProdukHukumBinding

class KelolaAdapter(
    private var items: List<ProdukHukumEntity>,
    private val onEditClick: (ProdukHukumEntity) -> Unit,
    private val onDeleteClick: (ProdukHukumEntity) -> Unit,
    private val onItemClick: (ProdukHukumEntity) -> Unit
) : RecyclerView.Adapter<KelolaAdapter.ViewHolder>() {

    fun updateData(newItems: List<ProdukHukumEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKelolaProdukHukumBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemKelolaProdukHukumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProdukHukumEntity) {
            binding.imgDocIcon.setImageResource(item.iconRes)
            binding.tvDocName.text = item.name
            binding.tvDocNumber.text = "No. ${item.number} Tahun ${item.year}"
            binding.tvDocDesc.text = item.shortDescription
            binding.tvDocCategory.text = item.category

            val context = binding.root.context

            // Status Badge UI configuration
            if (item.isValid) {
                binding.tvStatus.text = "Berlaku"
                binding.tvStatus.setTextColor(context.resources.getColor(R.color.accent_green, context.theme))
                binding.cardStatus.setCardBackgroundColor(context.resources.getColor(R.color.primary_maroon_light, context.theme))
            } else {
                binding.tvStatus.text = "Tidak Berlaku"
                binding.tvStatus.setTextColor(context.resources.getColor(R.color.accent_red, context.theme))
                binding.cardStatus.setCardBackgroundColor(context.resources.getColor(R.color.accent_red, context.theme).and(0x00FFFFFF).or(0x1AFF0000))
            }

            binding.btnEdit.setOnClickListener {
                onEditClick(item)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(item)
            }

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}
