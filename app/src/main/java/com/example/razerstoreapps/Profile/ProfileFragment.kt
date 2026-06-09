package com.example.razerstoreapps.Profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.razerstoreapps.AuthActivity
import com.example.razerstoreapps.R
import com.example.razerstoreapps.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadProfileData()

        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        binding.btnProfileLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun loadProfileData() {
        val sharedPref = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val savedUsername = sharedPref.getString("username", "Pengguna") ?: "Pengguna"

        val fullName = sharedPref.getString("profile_full_name", savedUsername) ?: savedUsername
        val role = sharedPref.getString("profile_role", "Masyarakat Bina Desa") ?: "Masyarakat Bina Desa"
        val phone = sharedPref.getString("profile_phone", "0812-3456-7890") ?: "0812-3456-7890"
        val email = sharedPref.getString("profile_email", "$savedUsername@binadesa.go.id") ?: "$savedUsername@binadesa.go.id"
        val office = sharedPref.getString("profile_office", "Jl. Raya Bina Desa No. 10, Kantor Desa Bina Desa") ?: "Jl. Raya Bina Desa No. 10, Kantor Desa Bina Desa"

        binding.tvProfileName.text = fullName
        binding.tvProfileRole.text = "Jabatan: $role"
        binding.tvProfilePhone.text = phone
        binding.tvProfileEmail.text = email
        binding.tvProfileOffice.text = office
    }

    private fun showEditProfileDialog() {
        val sharedPref = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val savedUsername = sharedPref.getString("username", "Pengguna") ?: "Pengguna"

        val currentName = sharedPref.getString("profile_full_name", savedUsername) ?: savedUsername
        val currentRole = sharedPref.getString("profile_role", "Masyarakat Bina Desa") ?: "Masyarakat Bina Desa"
        val currentPhone = sharedPref.getString("profile_phone", "0812-3456-7890") ?: "0812-3456-7890"
        val currentEmail = sharedPref.getString("profile_email", "$savedUsername@binadesa.go.id") ?: "$savedUsername@binadesa.go.id"
        val currentOffice = sharedPref.getString("profile_office", "Jl. Raya Bina Desa No. 10, Kantor Desa Bina Desa") ?: "Jl. Raya Bina Desa No. 10, Kantor Desa Bina Desa"

        // Build Dialog view dynamically
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val etName = createDialogEditText("Nama Lengkap", currentName)
        val etRole = createDialogEditText("Jabatan", currentRole)
        val etPhone = createDialogEditText("Nomor Telepon", currentPhone)
        val etEmail = createDialogEditText("Email", currentEmail)
        val etOffice = createDialogEditText("Alamat Kantor Desa", currentOffice)

        layout.addView(etName)
        layout.addView(etRole)
        layout.addView(etPhone)
        layout.addView(etEmail)
        layout.addView(etOffice)

        AlertDialog.Builder(requireContext())
            .setTitle("Ubah Profil Pengguna")
            .setView(layout)
            .setPositiveButton("Simpan") { dialog, _ ->
                val name = etName.text.toString().trim()
                val role = etRole.text.toString().trim()
                val phone = etPhone.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val office = etOffice.text.toString().trim()

                if (name.isEmpty() || role.isEmpty() || phone.isEmpty() || email.isEmpty() || office.isEmpty()) {
                    Toast.makeText(requireContext(), "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                sharedPref.edit().apply {
                    putString("profile_full_name", name)
                    putString("profile_role", role)
                    putString("profile_phone", phone)
                    putString("profile_email", email)
                    putString("profile_office", office)
                    apply()
                }

                Toast.makeText(requireContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                loadProfileData()
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun createDialogEditText(hint: String, value: String): EditText {
        return EditText(requireContext()).apply {
            this.hint = hint
            setText(value)
            setSingleLine(true)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 24
            }
            layoutParams = params
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar dari akun?")
            .setPositiveButton("Ya") { dialog, _ ->
                val sharedPref = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
                sharedPref.edit().clear().apply()

                dialog.dismiss()

                val intent = Intent(requireContext(), AuthActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                requireActivity().finish()
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