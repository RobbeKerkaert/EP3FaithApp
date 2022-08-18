package com.example.faith.ui.quotes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faith.domain.Quote
import com.example.faith.repository.QuoteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuotesViewModel(private val repository: QuoteRepository) : ViewModel() {

    var quote = MutableLiveData<String>()

    fun getRandomQuote() {
        var request: Call<List<Quote>> = repository.getRandomQuote()
        request.enqueue(object : Callback<List<Quote>> {
            override fun onResponse(call: Call<List<Quote>>, response: Response<List<Quote>>) {
                quote.value = response.body()?.first()?.q.toString()
                println("It worked: ${response.message()}")
                println(quote.value)
            }

            override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                println("It failed: ${t.message}")
            }

        })
    }
}