package com.example.razerstoreapps.data.news

class NewsRepository(private val apiService: ApiService) {
    suspend fun getTechnologyNews(): List<Article> {
        val response = apiService.getTechnologyNews()
        return response.articles
    }
}
