package com.example.razerstoreapps.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ProdukHukumDao {
    @Query("SELECT * FROM produk_hukum")
    List<ProdukHukumEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProdukHukumEntity item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProdukHukumEntity> items);

    @Update
    void update(ProdukHukumEntity item);

    @Delete
    void delete(ProdukHukumEntity item);
}
