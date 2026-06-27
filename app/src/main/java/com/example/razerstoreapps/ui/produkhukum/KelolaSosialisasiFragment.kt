package com.example.razerstoreapps.ui.produkhukum

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.razerstoreapps.R
import com.example.razerstoreapps.data.database.AppDatabase
import com.example.razerstoreapps.data.database.ProdukHukumEntity
import com.example.razerstoreapps.data.database.SosialisasiEntity
import com.example.razerstoreapps.databinding.DialogAddEditSosialisasiBinding
import com.example.razerstoreapps.databinding.FragmentKelolaSosialisasiBinding
import com.example.razerstoreapps.ui.adapter.KelolaSosialisasiAdapter
import com.example.razerstoreapps.data.notification.NotificationHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class KelolaSosialisasiFragment : Fragment() {

    private var _binding: FragmentKelolaSosialisasiBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: KelolaSosialisasiAdapter
    private var allItems = listOf<SosialisasiEntity>()
    private var docMap = mapOf<String, String>()
    private var allDocs = listOf<ProdukHukumEntity>()

    private val database by lazy {
        AppDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKelolaSosialisasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadData()

        // Setup Search Listener
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // FAB to add new data
        binding.fabAdd.setOnClickListener {
            showAddEditDialog(null)
        }
    }

    private fun setupRecyclerView() {
        adapter = KelolaSosialisasiAdapter(
            items = emptyList(),
            docMap = emptyMap(),
            onEditClick = { item -> showAddEditDialog(item) },
            onDeleteClick = { item -> confirmDelete(item) }
        )
        binding.rvKelola.layoutManager = LinearLayoutManager(requireContext())
        binding.rvKelola.adapter = adapter
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                // Fetch data from database on IO thread
                val (events, docs) = withContext(Dispatchers.IO) {
                    val eventsList = database.sosialisasiDao().all
                    val docsList = database.produkHukumDao().all
                    Pair(eventsList, docsList)
                }
                allItems = events
                allDocs = docs
                docMap = docs.associate { it.id to it.name }
                updateUI()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat data database: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI() {
        val query = binding.etSearch.text.toString()
        if (query.isNotEmpty()) {
            filterData(query)
        } else {
            adapter.updateData(allItems, docMap)
            toggleEmptyState(allItems.isEmpty())
        }
    }

    private fun filterData(query: String) {
        val filtered = allItems.filter {
            it.title.contains(query, ignoreCase = true) ||
            it.location.contains(query, ignoreCase = true) ||
            it.speaker.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
        adapter.updateData(filtered, docMap)
        toggleEmptyState(filtered.isEmpty())
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvKelola.visibility = View.GONE
            binding.layoutEmpty.visibility = View.VISIBLE
        } else {
            binding.rvKelola.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showAddEditDialog(item: SosialisasiEntity?) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogAddEditSosialisasiBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        // Setup Date Picker
        val calendar = Calendar.getInstance()
        dialogBinding.etDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val months = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
                    val formattedDate = "$dayOfMonth ${months[month]} $year"
                    dialogBinding.etDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Setup Time Picker
        dialogBinding.etTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                    dialogBinding.etTime.setText(formattedTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        // Setup Related Regulation Spinner
        val docTitles = mutableListOf<String>()
        docTitles.add("-- Pilih Regulasi Terkait --")
        docTitles.addAll(allDocs.map { "${it.name} No. ${it.number} (${it.year})" })

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, docTitles)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spinnerRelatedDoc.adapter = spinnerAdapter

        // Populate fields if editing
        if (item != null) {
            dialogBinding.tvFormTitle.text = "Edit Sosialisasi Hukum"
            dialogBinding.etTitle.setText(item.title)
            dialogBinding.etDate.setText(item.date)
            dialogBinding.etTime.setText(item.time)
            dialogBinding.etLocation.setText(item.location)
            dialogBinding.etSpeaker.setText(item.speaker)
            dialogBinding.etDescription.setText(item.description)

            val position = allDocs.indexOfFirst { it.id == item.relatedDocId }
            if (position != -1) {
                dialogBinding.spinnerRelatedDoc.setSelection(position + 1)
            }
        } else {
            dialogBinding.tvFormTitle.text = "Tambah Sosialisasi Baru"
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            val title = dialogBinding.etTitle.text.toString().trim()
            val date = dialogBinding.etDate.text.toString().trim()
            val time = dialogBinding.etTime.text.toString().trim()
            val location = dialogBinding.etLocation.text.toString().trim()
            val speaker = dialogBinding.etSpeaker.text.toString().trim()
            val description = dialogBinding.etDescription.text.toString().trim()
            val selectedIndex = dialogBinding.spinnerRelatedDoc.selectedItemPosition

            // Validation
            if (title.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty() || speaker.isEmpty() || description.isEmpty()) {
                Toast.makeText(requireContext(), "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val relatedDocId = if (selectedIndex > 0) allDocs[selectedIndex - 1].id else ""

            val eventId = item?.id ?: System.currentTimeMillis().toString()

            val newEvent = SosialisasiEntity(
                eventId,
                title,
                description,
                date,
                time,
                location,
                speaker,
                relatedDocId
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (item != null) {
                        database.sosialisasiDao().update(newEvent)
                    } else {
                        database.sosialisasiDao().insert(newEvent)
                    }
                }
                Toast.makeText(requireContext(), "Jadwal sosialisasi berhasil disimpan!", Toast.LENGTH_SHORT).show()

                // Trigger notification for adding new event
                if (item == null) {
                    NotificationHelper.showNotification(
                        requireContext(),
                        "📅 Sosialisasi Baru Dijadwalkan!",
                        "Sosialisasi '$title' akan diselenggarakan pada $date pukul $time di $location."
                    )
                }

                dialog.dismiss()
                loadData()
            }
        }

        dialog.show()
    }

    private fun confirmDelete(item: SosialisasiEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Data")
            .setMessage("Apakah Anda yakin ingin menghapus jadwal sosialisasi '${item.title}'?")
            .setPositiveButton("Ya") { dialog, _ ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        database.sosialisasiDao().delete(item)
                    }
                    Toast.makeText(requireContext(), "Jadwal berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    loadData()
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
