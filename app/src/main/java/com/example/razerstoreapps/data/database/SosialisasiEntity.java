package com.example.razerstoreapps.data.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "sosialisasi_event")
public class SosialisasiEntity implements Serializable {
    @PrimaryKey
    @NonNull
    public String id = "";
    
    public String title;
    public String description;
    public String date;
    public String time;
    public String location;
    public String speaker;
    public String relatedDocId; // Reference to related ProdukHukumEntity

    @Ignore
    public SosialisasiEntity() {}

    public SosialisasiEntity(@NonNull String id, String title, String description, String date, String time, String location, String speaker, String relatedDocId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.speaker = speaker;
        this.relatedDocId = relatedDocId;
    }
}
