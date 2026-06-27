package com.example.razerstoreapps.ui.detail

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.razerstoreapps.R
import com.example.razerstoreapps.data.database.ProdukHukum
import com.example.razerstoreapps.databinding.ActivityDetailProdukHukumBinding
import com.example.razerstoreapps.data.notification.NotificationHelper
import java.util.Calendar

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

        // Set up Reminder button listener
        binding.btnSetReminder.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
                    return@setOnClickListener
                }
            }
            showTimePicker(doc)
        }
    }

    private fun showTimePicker(doc: ProdukHukum) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val reminderCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // If time is in the past, schedule for tomorrow
            if (reminderCalendar.timeInMillis <= System.currentTimeMillis()) {
                reminderCalendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            // Schedule alarm using AlarmManager
            NotificationHelper.scheduleReminder(
                this,
                doc.id,
                doc.name,
                reminderCalendar.timeInMillis
            )

            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            Toast.makeText(this, "Pengingat baca untuk '${doc.name}' diatur pada jam $formattedTime", Toast.LENGTH_LONG).show()

        }, hour, minute, true)

        timePickerDialog.show()
    }
}

