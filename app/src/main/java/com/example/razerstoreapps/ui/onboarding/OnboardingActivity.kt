package com.example.razerstoreapps.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.razerstoreapps.AuthActivity
import com.example.razerstoreapps.R
import com.example.razerstoreapps.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    private val onboardingItems = listOf(
        OnboardingItem(
            title = "Produk Hukum Desa",
            description = "Akses berbagai peraturan dan dokumen hukum desa secara digital.",
            imageRes = R.drawable.ic_ob_1
        ),
        OnboardingItem(
            title = "Informasi Terintegrasi",
            description = "Temukan informasi hukum desa dengan cepat dan mudah.",
            imageRes = R.drawable.ic_ob_2
        ),
        OnboardingItem(
            title = "Berita Desa",
            description = "Dapatkan berita dan informasi terbaru seputar desa.",
            imageRes = R.drawable.ic_ob_3
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnboardingAdapter(onboardingItems)
        binding.viewPager.adapter = adapter

        setupIndicators()
        setCurrentIndicator(0)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)

                if (position == onboardingItems.size - 1) {
                    binding.btnNext.visibility = View.GONE
                    binding.btnSkip.visibility = View.GONE
                    binding.btnMulai.visibility = View.VISIBLE
                } else {
                    binding.btnNext.visibility = View.VISIBLE
                    binding.btnSkip.visibility = View.VISIBLE
                    binding.btnMulai.visibility = View.GONE
                }
            }
        })

        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < onboardingItems.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            }
        }

        binding.btnSkip.setOnClickListener {
            finishOnboarding()
        }

        binding.btnMulai.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(onboardingItems.size)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }

        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(
                androidx.core.content.ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.dot_inactive
                )
            )
            indicators[i]?.layoutParams = layoutParams
            binding.indicatorContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorContainer.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    androidx.core.content.ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.dot_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    androidx.core.content.ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.dot_inactive
                    )
                )
            }
        }
    }

    private fun finishOnboarding() {
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        sharedPref.edit().apply {
            putBoolean("is_onboarding_completed", true)
            apply()
        }
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}

