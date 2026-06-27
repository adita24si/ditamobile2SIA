package com.example.razerstoreapps.ui.produkhukum

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.razerstoreapps.data.database.AppDatabase
import com.example.razerstoreapps.data.database.SosialisasiEntity
import com.example.razerstoreapps.databinding.FragmentSosialisasiBinding
import com.example.razerstoreapps.data.notification.NotificationHelper
import com.example.razerstoreapps.ui.adapter.SosialisasiAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class SosialisasiFragment : Fragment() {

    private var _binding: FragmentSosialisasiBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SosialisasiAdapter
    private val database by lazy { AppDatabase.getDatabase(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSosialisasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadData()
    }

    private fun setupRecyclerView() {
        binding.rvSosialisasi.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Fetch data from database
                val (events, docMap) = withContext(Dispatchers.IO) {
                    val allEvents = database.sosialisasiDao().all
                    val allDocs = database.produkHukumDao().all
                    val map = allDocs.associate { it.id to it.name }
                    Pair(allEvents, map)
                }

                adapter = SosialisasiAdapter(events, docMap) { event ->
                    checkPermissionAndShowTimePicker(event)
                }
                binding.rvSosialisasi.adapter = adapter
                
                binding.layoutEmpty.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat jadwal sosialisasi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionAndShowTimePicker(event: SosialisasiEntity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), 102)
                return
            }
        }
        showTimePicker(event)
    }

    private fun showTimePicker(event: SosialisasiEntity) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val reminderCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (reminderCalendar.timeInMillis <= System.currentTimeMillis()) {
                reminderCalendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            // Schedule the event reminder
            NotificationHelper.scheduleReminder(
                requireContext(),
                event.id,
                "Sosialisasi: ${event.title}",
                reminderCalendar.timeInMillis
            )

            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            Toast.makeText(requireContext(), "Pengingat untuk '${event.title}' diatur pada jam $formattedTime", Toast.LENGTH_LONG).show()

        }, hour, minute, true)

        timePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
