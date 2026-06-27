package com.example.razerstoreapps.ui.produkhukum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.razerstoreapps.R
import com.example.razerstoreapps.databinding.FragmentKelolaBinding
import com.google.android.material.tabs.TabLayout

class KelolaFragment : Fragment() {

    private var _binding: FragmentKelolaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKelolaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()
    }

    private fun setupTabs() {
        // Add Tabs
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Regulasi"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Sosialisasi Hukum"))

        // Load Default Fragment
        if (childFragmentManager.findFragmentById(R.id.kelolaContainer) == null) {
            replaceChildFragment(KelolaProdukHukumFragment())
        }

        // Listener for Tab Selection
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> replaceChildFragment(KelolaProdukHukumFragment())
                    1 -> replaceChildFragment(KelolaSosialisasiFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun replaceChildFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.kelolaContainer, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
