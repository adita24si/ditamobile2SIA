package com.example.razerstoreapps.data.repository

import com.example.razerstoreapps.data.api.ApiService
import com.example.razerstoreapps.data.model.Article

class NewsRepository(private val apiService: ApiService) {
    suspend fun getTechnologyNews(): List<Article> {
        val response = apiService.getTechnologyNews()
        return response.articles
    }
}
