package com.example.razerstoreapps.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.razerstoreapps.R
import com.example.razerstoreapps.data.model.ProdukHukum
import com.example.razerstoreapps.databinding.ActivityDetailProdukHukumBinding

class DetailProdukHukumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProdukHukumBinding

    companion object {
        const val EXTRA_DOC = "extra_doc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProdukHukumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar back navigation
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarDetail.setNavigationOnClickListener {
            onBackPressed()
        }

        // Extract passed document
        val doc = intent.getSerializableExtra(EXTRA_DOC) as? ProdukHukum
        if (doc != null) {
            populateViews(doc)
        } else {
            Toast.makeText(this, "Gagal memuat detail dokumen", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun populateViews(doc: ProdukHukum) {
        binding.tvDetailTitle.text = doc.name
        binding.tvDetailDocNumber.text = "Nomor Dokumen: ${doc.number}/${doc.category.uppercase().replace(" ", "")}/${doc.year}"
        binding.tvDetailCategory.text = doc.category
        binding.tvDetailCategoryRow.text = doc.category
        binding.tvDetailYear.text = doc.year
        binding.tvDetailDescription.text = doc.longDescription

        // Status Badge UI configuration
        if (doc.isValid) {
            binding.tvDetailStatus.text = "Berlaku"
            binding.tvDetailStatus.setTextColor(resources.getColor(R.color.accent_green, theme))
            binding.cardStatusBadge.setCardBackgroundColor(resources.getColor(R.color.primary_maroon_light, theme)) // lightweight background
        } else {
            binding.tvDetailStatus.text = "Tidak Berlaku"
            binding.tvDetailStatus.setTextColor(resources.getColor(R.color.accent_red, theme))
            binding.cardStatusBadge.setCardBackgroundColor(resources.getColor(R.color.accent_red, theme).and(0x00FFFFFF).or(0x1AFF0000)) // lightweight pink background
        }

        // Download button listener
        binding.btnDownloadPdf.setOnClickListener {
            Toast.makeText(this, "Mengunduh dokumen PDF ${doc.name}...", Toast.LENGTH_SHORT).show()
        }
    }
}
