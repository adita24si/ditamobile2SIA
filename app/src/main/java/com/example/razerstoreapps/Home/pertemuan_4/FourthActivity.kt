package com.example.razerstoreapps.Home.pertemuan_4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.razerstoreapps.MainActivity
import com.example.razerstoreapps.R
import com.example.razerstoreapps.databinding.ActivityFourthBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class FourthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFourthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("onCreate", " Fourth_activity dibuat pertama kali")
        enableEdgeToEdge()
        binding = ActivityFourthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

            val name = intent.getStringExtra("name")
            val from = intent.getStringExtra("from")
            val age = intent.getIntExtra("age",0)
            Log.e("Data Intent","Nama: $name , Usia: $age, Asal: $from")

        binding.btnKembali.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnSnackBar.setOnClickListener {
            Snackbar.make(binding.root, "Ini adalah Snackbar", Snackbar.LENGTH_SHORT)
                .setAction("Tutup"){
                    Log.e("Info Snackbar","Snackbar ditutup")
                }
                .show()
        }

        binding.btnShowAlertDialog.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin melanjutkan?")
                .setPositiveButton("Ya") { dialog, _ ->
                    dialog.dismiss()
                    Log.e("Info Dialog","Anda memilih Ya!")
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                    Log.e("Info Dialog","Anda memilih Tidak!")
                }
                .show()
        }

    }







    override fun onStart() {
        super.onStart()
        Log.e("onStart", "onStart: Fourth_activity terlihat di layar")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy", " Fourth_activity dihapus dari stack")
    }
}