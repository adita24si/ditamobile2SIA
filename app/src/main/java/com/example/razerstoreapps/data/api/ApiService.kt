package com.example.razerstoreapps.data.api

import com.example.razerstoreapps.data.model.NewsResponse
import retrofit2.http.GET

interface ApiService {
    @GET("top-headlines/category/technology/us.json")
    suspend fun getTechnologyNews(): NewsResponse
}
