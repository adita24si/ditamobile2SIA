package com.example.razerstoreapps

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.razerstoreapps.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
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

        val isOnboardingCompleted = sharedPref.getBoolean(
            "is_onboarding_completed",
            false
        )

        val isLogin = sharedPref.getBoolean(
            "isLogin",
            false
        )

        // Splash delay + redirect
        lifecycleScope.launch {

            delay(2000)

            if (!isOnboardingCompleted) {
                startActivity(
                    Intent(
                        this@SplashScreenActivity,
                        com.example.razerstoreapps.ui.onboarding.OnboardingActivity::class.java
                    )
                )
            } else if (isLogin) {
                startActivity(
                    Intent(
                        this@SplashScreenActivity,
                        BaseActivity::class.java
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