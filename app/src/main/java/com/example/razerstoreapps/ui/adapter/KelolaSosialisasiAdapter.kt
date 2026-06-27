package com.example.razerstoreapps.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.razerstoreapps.data.database.SosialisasiEntity
import com.example.razerstoreapps.databinding.ItemKelolaSosialisasiBinding

class KelolaSosialisasiAdapter(
    private var items: List<SosialisasiEntity>,
    private var docMap: Map<String, String>,
    private val onEditClick: (SosialisasiEntity) -> Unit,
    private val onDeleteClick: (SosialisasiEntity) -> Unit
) : RecyclerView.Adapter<KelolaSosialisasiAdapter.ViewHolder>() {

    fun updateData(newItems: List<SosialisasiEntity>, newDocMap: Map<String, String>) {
        items = newItems
        docMap = newDocMap
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKelolaSosialisasiBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemKelolaSosialisasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SosialisasiEntity) {
            binding.tvEventTitle.text = item.title
            binding.tvEventDateTime.text = "${item.date} pukul ${item.time}"
            binding.tvEventLocation.text = item.location
            binding.tvEventSpeaker.text = item.speaker
            binding.tvEventDesc.text = item.description

            val docName = docMap[item.relatedDocId] ?: "Regulasi Terkait"
            binding.tvRelatedDoc.text = "Terkait: $docName"

            binding.btnEdit.setOnClickListener {
                onEditClick(item)
            }

            binding.btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }
}
