package com.example.razerstoreapps

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.razerstoreapps.databinding.ActivityBaseBinding
import com.example.razerstoreapps.About.AboutFragment
import com.example.razerstoreapps.Home.tugasp6.HomeFragment
import com.example.razerstoreapps.Profile.ProfileFragment

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // 🔒 CEK LOGIN (WAJIB)
        if (!sharedPref.getBoolean("isLogin", false)) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // edge-to-edge fix
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ambil username (lebih aman dari SharedPreferences)
        val username = sharedPref.getString("username", "Pengguna") ?: "Pengguna"

        // 🔥 hindari reload fragment saat rotate
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment.newInstance(username))
        }

        // bottom navigation
        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment.newInstance(username))
                    true
                }
                R.id.about -> {
                    replaceFragment(AboutFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}