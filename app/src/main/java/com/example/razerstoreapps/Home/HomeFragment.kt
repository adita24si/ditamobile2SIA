package com.example.razerstoreapps.Home.tugasp6

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.razerstoreapps.BaseActivity
import com.example.razerstoreapps.BinaDesaWebView
import com.example.razerstoreapps.R
import com.example.razerstoreapps.data.api.RetrofitClient
import com.example.razerstoreapps.data.repository.NewsRepository
import com.example.razerstoreapps.databinding.FragmentHomeBinding
import com.example.razerstoreapps.ui.adapter.NewsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val newsRepository by lazy {
        NewsRepository(RetrofitClient.apiService)
    }

    companion object {
        fun newInstance(username: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            bundle.putString("username", username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString("username") ?: "Pengguna"
        binding.tvUsername.text = "Halo, $username 👋"

        // Set up Menu actions
        binding.menuProdukHukum.setOnClickListener {
            // Programmatically navigate to BottomNav's Produk Hukum item
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavView)
            bottomNav?.selectedItemId = R.id.produk_hukum
        }

        binding.menuInfoDesa.setOnClickListener {
            Toast.makeText(requireContext(), "Informasi Desa Bina Desa", Toast.LENGTH_SHORT).show()
        }

        binding.menuPortalDesa.setOnClickListener {
            // Launch webview to default site
            startActivity(Intent(requireContext(), BinaDesaWebView::class.java))
        }

        binding.menuDokumentasi.setOnClickListener {
            Toast.makeText(requireContext(), "Galeri Dokumentasi Desa", Toast.LENGTH_SHORT).show()
        }

        // Set up News RecyclerView
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        
        binding.btnRetryNews.setOnClickListener {
            fetchNews()
        }

        fetchNews()
    }

    private fun fetchNews() {
        showLoadingState()
        lifecycleScope.launch {
            try {
                val articles = newsRepository.getTechnologyNews()
                if (articles.isEmpty()) {
                    showEmptyState()
                } else {
                    showContentState()
                    binding.rvNews.adapter = NewsAdapter(articles) { article ->
                        // On article click, open url in external browser
                        val articleUrl = article.url
                        if (!articleUrl.isNullOrEmpty()) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(requireContext(), "Gagal membuka link berita", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                showErrorState()
            }
        }
    }

    private fun showLoadingState() {
        binding.progressLoadingNews.visibility = View.VISIBLE
        binding.rvNews.visibility = View.GONE
        binding.layoutErrorNews.visibility = View.GONE
        binding.layoutEmptyNews.visibility = View.GONE
    }

    private fun showContentState() {
        binding.progressLoadingNews.visibility = View.GONE
        binding.rvNews.visibility = View.VISIBLE
        binding.layoutErrorNews.visibility = View.GONE
        binding.layoutEmptyNews.visibility = View.GONE
    }

    private fun showEmptyState() {
        binding.progressLoadingNews.visibility = View.GONE
        binding.rvNews.visibility = View.GONE
        binding.layoutErrorNews.visibility = View.GONE
        binding.layoutEmptyNews.visibility = View.VISIBLE
    }

    private fun showErrorState() {
        binding.progressLoadingNews.visibility = View.GONE
        binding.rvNews.visibility = View.GONE
        binding.layoutErrorNews.visibility = View.VISIBLE
        binding.layoutEmptyNews.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}