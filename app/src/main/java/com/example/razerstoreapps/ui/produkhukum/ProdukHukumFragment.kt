package com.example.razerstoreapps.ui.produkhukum

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.razerstoreapps.R
import com.example.razerstoreapps.data.model.ProdukHukum
import com.example.razerstoreapps.databinding.FragmentProdukHukumBinding
import com.example.razerstoreapps.ui.adapter.ProdukHukumAdapter
import com.example.razerstoreapps.ui.detail.DetailProdukHukumActivity

class ProdukHukumFragment : Fragment() {

    private var _binding: FragmentProdukHukumBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProdukHukumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dummyDocs = getDummyProdukHukum()

        binding.rvProdukHukum.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProdukHukum.adapter = ProdukHukumAdapter(dummyDocs) { doc ->
            // Open Detail page
            val intent = Intent(requireContext(), DetailProdukHukumActivity::class.java).apply {
                putExtra(DetailProdukHukumActivity.EXTRA_DOC, doc)
            }
            startActivity(intent)
        }
    }

    private fun getDummyProdukHukum(): List<ProdukHukum> {
        val icon = R.drawable.ic_law
        return listOf(
            ProdukHukum(
                id = "1",
                name = "Peraturan Desa (Perdes)",
                number = "3",
                year = "2025",
                category = "Peraturan Desa",
                shortDescription = "Tentang Rencana Tata Ruang dan Pengelolaan Hutan Lindung Desa.",
                longDescription = "Peraturan Desa Nomor 3 Tahun 2025 menetapkan regulasi menyeluruh terkait pemanfaatan wilayah desa, perlindungan kelestarian hutan lindung di area desa, serta peruntukan lahan pertanian berkelanjutan bagi masyarakat.",
                isValid = true,
                iconRes = icon
            ),
            ProdukHukum(
                id = "2",
                name = "Peraturan Kepala Desa",
                number = "12",
                year = "2025",
                category = "Peraturan Kepala Desa",
                shortDescription = "Tentang Teknis Pembagian BLT Dana Desa Tahap Akhir.",
                longDescription = "Peraturan Kepala Desa Nomor 12 Tahun 2025 menjabarkan petunjuk teknis pelaksanaan dan penyaluran Bantuan Langsung Tunai (BLT) dari alokasi Dana Desa guna menjaga akuntabilitas dan pemerataan.",
                isValid = true,
                iconRes = icon
            ),
            ProdukHukum(
                id = "3",
                name = "Keputusan Kepala Desa",
                number = "45",
                year = "2025",
                category = "Keputusan Kepala Desa",
                shortDescription = "Tentang Pengangkatan Pengurus Posyandu Melati Indah.",
                longDescription = "Keputusan Kepala Desa Nomor 45 Tahun 2025 secara resmi mengangkat nama-nama terlampir sebagai anggota pengurus aktif unit layanan kesehatan Posyandu Melati Indah periode 2025-2027.",
                isValid = true,
                iconRes = icon
            ),
            ProdukHukum(
                id = "4",
                name = "RPJMDes",
                number = "1",
                year = "2024",
                category = "Rencana Pembangunan",
                shortDescription = "Rencana Pembangunan Jangka Menengah Desa 2024-2030.",
                longDescription = "Dokumen RPJMDes (Rencana Pembangunan Jangka Menengah Desa) memuat arah pembangunan infrastruktur fisik, peningkatan kapasitas SDM, dan pemberdayaan ekonomi desa untuk masa bakti enam tahun ke depan.",
                isValid = true,
                iconRes = icon
            ),
            ProdukHukum(
                id = "5",
                name = "RKPDes",
                number = "2",
                year = "2025",
                category = "Rencana Kerja Pemerintah",
                shortDescription = "Rencana Kerja Pemerintah Desa Tahun Anggaran 2025.",
                longDescription = "RKPDes merupakan jabaran tahunan dari RPJMDes, memuat daftar program prioritas desa yang akan dibiayai menggunakan APBDes tahun anggaran berjalan.",
                isValid = true,
                iconRes = icon
            ),
            ProdukHukum(
                id = "6",
                name = "APBDes",
                number = "4",
                year = "2025",
                category = "Anggaran Desa",
                shortDescription = "Anggaran Pendapatan dan Belanja Desa Bina Desa TA 2025.",
                longDescription = "Anggaran Pendapatan dan Belanja Desa (APBDes) merangkum estimasi penerimaan dana dari ADD, Dana Desa, PADes, serta alokasi pengeluaran untuk pembangunan desa.",
                isValid = true,
                iconRes = icon
            ),
            ProdukHukum(
                id = "7",
                name = "Peraturan BPD",
                number = "2",
                year = "2024",
                category = "Peraturan BPD",
                shortDescription = "Tentang Tata Tertib Musyawarah dan Pengawasan Desa.",
                longDescription = "Peraturan Badan Permusyawaratan Desa (BPD) mengatur mekanisme persidangan internal, tata tertib musyawarah desa bersama warga, serta fungsi pengawasan kinerja kepala desa.",
                isValid = true,
                iconRes = icon
            ),
            ProdukHukum(
                id = "8",
                name = "Anggaran Dasar BUMDes",
                number = "1",
                year = "2023",
                category = "AD/ART BUMDes",
                shortDescription = "Anggaran Dasar Badan Usaha Milik Desa Bina Sejahtera.",
                longDescription = "Anggaran Dasar (AD) BUMDes Bina Sejahtera menetapkan landasan hukum, jenis usaha (simpan pinjam, pengelolaan air bersih, pasar desa), serta struktur permodalan awal BUMDes.",
                isValid = true,
                iconRes = icon
            ),
            ProdukHukum(
                id = "9",
                name = "Anggaran Rumah Tangga BUMDes",
                number = "2",
                year = "2023",
                category = "AD/ART BUMDes",
                shortDescription = "Anggaran Rumah Tangga BUMDes Bina Sejahtera.",
                longDescription = "Anggaran Rumah Tangga (ART) BUMDes Bina Sejahtera mengatur pembagian hasil usaha (sisa hasil usaha), mekanisme operasional unit usaha, dan rekrutmen pengurus.",
                isValid = true,
                iconRes = icon
            ),
            ProdukHukum(
                id = "10",
                name = "Peraturan Kerja Sama Desa",
                number = "5",
                year = "2025",
                category = "Kerja Sama Desa",
                shortDescription = "Kerja Sama Batas Wilayah dan Ekonomi dengan Desa Makmur.",
                longDescription = "Peraturan ini mengatur kerja sama antardesa di bidang ekonomi kreatif serta penetapan batas teritorial administrasi guna meminimalisir sengketa lahan.",
                isValid = false, // Contoh tidak berlaku / dicabut
                iconRes = icon
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
