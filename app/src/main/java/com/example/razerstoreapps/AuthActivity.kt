package com.example.razerstoreapps

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.razerstoreapps.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // 🔒 AUTO LOGIN (hindari double open)
        if (sharedPref.getBoolean("isLogin", false)) {
            navigateToBase(sharedPref.getString("username", "Pengguna"))
            return
        }

        binding.btnLogin.setOnClickListener {

            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (username.isEmpty()) {
                binding.edtUsername.error = "Username wajib diisi"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edtPassword.error = "Password wajib diisi"
                return@setOnClickListener
            }

            // login sederhana
            if (username == password) {

                sharedPref.edit().apply {
                    putBoolean("isLogin", true)
                    putString("username", username)
                    apply()
                }

                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()

                navigateToBase(username)

            } else {
                Toast.makeText(this, "Username atau Password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToBase(username: String?) {
        val intent = Intent(this, BaseActivity::class.java).apply {
            putExtra("extra_username", username ?: "Pengguna")

            // 🔥 clear back stack biar tidak bisa balik ke login
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)
        finish()
    }
}