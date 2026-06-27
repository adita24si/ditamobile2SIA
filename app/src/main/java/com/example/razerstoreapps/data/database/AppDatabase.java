package com.example.razerstoreapps.data.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ProdukHukumEntity.class, SosialisasiEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract ProdukHukumDao produkHukumDao();
    public abstract SosialisasiDao sosialisasiDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "produk_hukum_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Prepopulate database in background thread
                                    new Thread(() -> {
                                        AppDatabase database = getDatabase(context);
                                        database.produkHukumDao().insertAll(getPrepopulatedDocs());
                                        database.sosialisasiDao().insertAll(getPrepopulatedEvents());
                                    }).start();
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static java.util.List<ProdukHukumEntity> getPrepopulatedDocs() {
        java.util.List<ProdukHukumEntity> list = new java.util.ArrayList<>();
        int icon = com.example.razerstoreapps.R.drawable.ic_law;
        list.add(new ProdukHukumEntity("1", "Peraturan Desa (Perdes)", "3", "2025", "Peraturan Desa", "Tentang Rencana Tata Ruang dan Pengelolaan Hutan Lindung Desa.", "Peraturan Desa Nomor 3 Tahun 2025 menetapkan regulasi menyeluruh terkait pemanfaatan wilayah desa, perlindungan kelestarian hutan lindung di area desa, serta peruntukan lahan pertanian berkelanjutan bagi masyarakat.", true, icon));
        list.add(new ProdukHukumEntity("2", "Peraturan Kepala Desa", "12", "2025", "Peraturan Kepala Desa", "Tentang Teknis Pembagian BLT Dana Desa Tahap Akhir.", "Peraturan Kepala Desa Nomor 12 Tahun 2025 menetapkan petunjuk teknis penyaluran BLT Dana Desa.", true, icon));
        list.add(new ProdukHukumEntity("3", "Keputusan Kepala Desa", "45", "2025", "Keputusan Kepala Desa", "Tentang Pengangkatan Pengurus Posyandu Melati Indah.", "Keputusan Kepala Desa Nomor 45 Tahun 2025 tentang pengangkatan pengurus Posyandu Melati Indah periode 2025-2027.", true, icon));
        list.add(new ProdukHukumEntity("4", "RPJMDes", "1", "2024", "Rencana Pembangunan", "Rencana Pembangunan Jangka Menengah Desa 2024-2030.", "RPJMDes memuat program prioritas desa jangka menengah 2024-2030.", true, icon));
        list.add(new ProdukHukumEntity("5", "RKPDes", "2", "2025", "Rencana Kerja Pemerintah", "Rencana Kerja Pemerintah Desa Tahun Anggaran 2025.", "RKPDes merupakan penjabaran tahunan dari RPJMDes untuk rencana kerja 2025.", true, icon));
        list.add(new ProdukHukumEntity("6", "APBDes", "4", "2025", "Anggaran Desa", "Anggaran Pendapatan dan Belanja Desa Bina Desa TA 2025.", "APBDes merangkum anggaran pendapatan dan belanja desa tahun anggaran 2025.", true, icon));
        list.add(new ProdukHukumEntity("7", "Peraturan BPD", "2", "2024", "Peraturan BPD", "Tentang Tata Tertib Musyawarah dan Pengawasan Desa.", "Peraturan BPD mengatur tata tertib musyawarah dan pengawasan di desa.", true, icon));
        return list;
    }

    private static java.util.List<SosialisasiEntity> getPrepopulatedEvents() {
        java.util.List<SosialisasiEntity> list = new java.util.ArrayList<>();
        list.add(new SosialisasiEntity("101", "Sosialisasi Perdes Hutan Lindung", "Sosialisasi mengenai batas pemanfaatan wilayah hutan lindung desa serta penertiban penebangan liar sesuai Perdes No. 3.", "30 Juni 2026", "09:00", "Balai Pertemuan Warga Dusun II", "Bapak Kepala Desa & Dinas Kehutanan", "1"));
        list.add(new SosialisasiEntity("102", "Penyuluhan Teknis BLT Dana Desa", "Penyuluhan teknis penyaluran bantuan langsung tunai agar tepat sasaran dan akuntabel sesuai Peraturan Kepala Desa No. 12.", "05 Juli 2026", "13:30", "Balai Desa Bina Desa", "Sekretaris Desa & Dinas Sosial", "2"));
        list.add(new SosialisasiEntity("103", "Musyawarah Kerja RPJMDes 2026", "Musyawarah bersama warga terkait penyusunan program prioritas infrastruktur jangka menengah.", "12 Juli 2026", "08:30", "Pendopo Kantor Desa", "BPD & Pendamping Desa", "4"));
        return list;
    }
}
