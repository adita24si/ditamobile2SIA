package com.example.razerstoreapps.data.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "produk_hukum")
public class ProdukHukumEntity implements Serializable {
    @PrimaryKey
    @NonNull
    public String id = "";
    
    public String name;
    public String number;
    public String year;
    public String category;
    public String shortDescription;
    public String longDescription;
    public boolean isValid;
    public int iconRes;

    @Ignore
    public ProdukHukumEntity() {}

    public ProdukHukumEntity(@NonNull String id, String name, String number, String year, String category, String shortDescription, String longDescription, boolean isValid, int iconRes) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.year = year;
        this.category = category;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.isValid = isValid;
        this.iconRes = iconRes;
    }
}
