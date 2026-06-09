package com.example.razerstoreapps.data.model

import java.io.Serializable

data class ProdukHukum(
    val id: String,
    val name: String,
    val number: String,
    val year: String,
    val category: String,
    val shortDescription: String,
    val longDescription: String,
    val isValid: Boolean, // Status Berlaku
    val iconRes: Int
) : Serializable
