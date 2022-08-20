package com.example.faith.ui.quotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.api.ApiService
import com.example.faith.databinding.QuotesFragmentBinding
import com.example.faith.repository.QuoteRepository

class QuotesFragment : Fragment() {

    private lateinit var viewModel: QuotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Inspirational Quotes"

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<QuotesFragmentBinding>(
            inflater,
            R.layout.quotes_fragment,
            container,
            false
        )
        val apiService = ApiService.getInstance()
        val repository = QuoteRepository(apiService)
        val viewModelFactory = QuotesViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory)[QuotesViewModel::class.java]

        viewModel.getRandomQuote()

        // Click listeners
        binding.newQuoteButton.setOnClickListener {
            viewModel.getRandomQuote()
            binding.quoteText.text = viewModel.quote.value
        }

        return binding.root
    }
}
