package com.example.razerstoreapps

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                bars.left,
                bars.top,
                bars.right,
                bars.bottom
            )
            insets
        }

        val sharedPref = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        val isLogin = sharedPref.getBoolean(
            "isLogin",
            false
        )

        // Splash delay + redirect
        lifecycleScope.launch {

            delay(2000)

            if (isLogin) {

                startActivity(
                    Intent(
                        this@SplashScreenActivity,
                        BinaDesa::class.java
                    )
                )

            } else {

                startActivity(
                    Intent(
                        this@SplashScreenActivity,
                        AuthActivity::class.java
                    )
                )
            }

            finish()
        }
    }
}