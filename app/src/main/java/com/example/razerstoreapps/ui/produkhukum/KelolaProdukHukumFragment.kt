package com.example.razerstoreapps.ui.produkhukum

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.razerstoreapps.R
import com.example.razerstoreapps.data.database.AppDatabase
import com.example.razerstoreapps.data.database.ProdukHukumEntity
import com.example.razerstoreapps.databinding.DialogAddEditProdukHukumBinding
import com.example.razerstoreapps.databinding.FragmentKelolaProdukHukumBinding
import com.example.razerstoreapps.ui.adapter.KelolaAdapter
import com.example.razerstoreapps.ui.detail.DetailProdukHukumActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.razerstoreapps.data.notification.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KelolaProdukHukumFragment : Fragment() {

    private var _binding: FragmentKelolaProdukHukumBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: KelolaAdapter
    private var allItems = listOf<ProdukHukumEntity>()

    private val database by lazy {
        AppDatabase.getDatabase(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKelolaProdukHukumBinding.inflate(inflater, container, false)
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
        adapter = KelolaAdapter(
            items = emptyList(),
            onEditClick = { item -> showAddEditDialog(item) },
            onDeleteClick = { item -> confirmDelete(item) },
            onItemClick = { item -> openDetail(item) }
        )
        binding.rvKelola.layoutManager = LinearLayoutManager(requireContext())
        binding.rvKelola.adapter = adapter
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                // Fetch data from database on IO thread
                val data = withContext(Dispatchers.IO) {
                    database.produkHukumDao().all
                }
                allItems = data
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
            adapter.updateData(allItems)
            toggleEmptyState(allItems.isEmpty())
        }
    }

    private fun filterData(query: String) {
        val filtered = allItems.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true) ||
            it.number.contains(query) ||
            it.year.contains(query)
        }
        adapter.updateData(filtered)
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

    private fun showAddEditDialog(item: ProdukHukumEntity?) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogAddEditProdukHukumBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        // Populate fields if editing
        if (item != null) {
            dialogBinding.tvFormTitle.text = "Edit Produk Hukum"
            dialogBinding.etName.setText(item.name)
            dialogBinding.etNumber.setText(item.number)
            dialogBinding.etYear.setText(item.year)
            dialogBinding.etCategory.setText(item.category)
            dialogBinding.etShortDesc.setText(item.shortDescription)
            dialogBinding.etLongDesc.setText(item.longDescription)
            dialogBinding.switchIsValid.isChecked = item.isValid
        } else {
            dialogBinding.tvFormTitle.text = "Tambah Produk Hukum Baru"
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnSave.setOnClickListener {
            val name = dialogBinding.etName.text.toString().trim()
            val number = dialogBinding.etNumber.text.toString().trim()
            val year = dialogBinding.etYear.text.toString().trim()
            val category = dialogBinding.etCategory.text.toString().trim()
            val shortDesc = dialogBinding.etShortDesc.text.toString().trim()
            val longDesc = dialogBinding.etLongDesc.text.toString().trim()
            val isValid = dialogBinding.switchIsValid.isChecked

            // Validation
            if (name.isEmpty() || number.isEmpty() || year.isEmpty() || category.isEmpty() || shortDesc.isEmpty() || longDesc.isEmpty()) {
                Toast.makeText(requireContext(), "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val docId = item?.id ?: System.currentTimeMillis().toString()
            val docIcon = item?.iconRes ?: R.drawable.ic_law

            val newDoc = ProdukHukumEntity(
                docId,
                name,
                number,
                year,
                category,
                shortDesc,
                longDesc,
                isValid,
                docIcon
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    if (item != null) {
                        database.produkHukumDao().update(newDoc)
                    } else {
                        database.produkHukumDao().insert(newDoc)
                    }
                }
                Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                
                // Trigger notification for adding new document
                if (item == null) {
                    NotificationHelper.showNotification(
                        requireContext(),
                        "📜 Regulasi Baru Diterbitkan!",
                        "Dokumen '$name' Nomor $number Tahun $year telah berhasil ditambahkan ke database lokal."
                    )
                }
                
                dialog.dismiss()
                loadData()
            }
        }

        dialog.show()
    }

    private fun confirmDelete(item: ProdukHukumEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Data")
            .setMessage("Apakah Anda yakin ingin menghapus '${item.name}'?")
            .setPositiveButton("Ya") { dialog, _ ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        database.produkHukumDao().delete(item)
                    }
                    Toast.makeText(requireContext(), "Data berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    loadData()
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun openDetail(item: ProdukHukumEntity) {
        // Map ProdukHukumEntity back to ProdukHukum domain model to pass to Detail Activity
        val domainDoc = com.example.razerstoreapps.data.database.ProdukHukum(
            id = item.id,
            name = item.name,
            number = item.number,
            year = item.year,
            category = item.category,
            shortDescription = item.shortDescription,
            longDescription = item.longDescription,
            isValid = item.isValid,
            iconRes = item.iconRes
        )
        val intent = Intent(requireContext(), DetailProdukHukumActivity::class.java).apply {
            putExtra(DetailProdukHukumActivity.EXTRA_DOC, domainDoc)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
