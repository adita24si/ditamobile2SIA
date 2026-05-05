package com.example.razerstoreapps

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.razerstoreapps.databinding.BinadesaWebviewBinding

class BinaDesaWebView : AppCompatActivity() {

    private lateinit var binding: BinadesaWebviewBinding

    private val PREF_NAME = "MyPrefs"
    private val KEY_URL = "last_url"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BinadesaWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(
            PREF_NAME,
            MODE_PRIVATE
        )

        // URL default
        val savedUrl = sharedPref.getString(
            KEY_URL,
            "https://adita.alwaysdata.net"
        )

        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            loadUrl(savedUrl!!)
        }

        // Tombol back
        binding.btnBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                finish()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        getSharedPreferences(
            PREF_NAME,
            MODE_PRIVATE
        ).edit()
            .putString(KEY_URL, binding.webView.url)
            .apply()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            finish()
        }
    }
}