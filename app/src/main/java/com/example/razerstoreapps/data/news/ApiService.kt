package com.example.razerstoreapps.data.news

import retrofit2.http.GET

interface ApiService {
    @GET("top-headlines/category/technology/us.json")
    suspend fun getTechnologyNews(): NewsResponse
}
