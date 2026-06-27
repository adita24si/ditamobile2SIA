package com.example.razerstoreapps.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface SosialisasiDao {
    @Query("SELECT * FROM sosialisasi_event")
    List<SosialisasiEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SosialisasiEntity event);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SosialisasiEntity> events);

    @Update
    void update(SosialisasiEntity event);

    @Delete
    void delete(SosialisasiEntity event);
}
