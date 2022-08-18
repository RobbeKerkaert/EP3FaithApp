package com.example.faith.api

import com.example.faith.domain.Quote
import com.example.faith.domain.QuoteList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET

interface ApiService {

    @GET("/api/random")
    fun getInspirationalQuote(): Call<List<Quote>>

    companion object {
        var apiService: ApiService? = null

        fun getInstance(): ApiService {
            if (apiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://zenquotes.io")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                apiService = retrofit.create(ApiService::class.java)
            }
            return apiService!!
        }
    }

}