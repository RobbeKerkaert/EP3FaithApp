package com.example.faith.repository

import com.example.faith.api.ApiService
import com.example.faith.domain.Quote
import com.example.faith.domain.QuoteList
import retrofit2.Call

class QuoteRepository(private val apiService: ApiService) {
    fun getRandomQuote(): Call<List<Quote>> {
        return apiService.getInspirationalQuote()
    }
}