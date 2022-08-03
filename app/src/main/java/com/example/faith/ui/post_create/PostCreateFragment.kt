package com.example.faith.ui.post_create

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.FragmentHomeBinding
import com.example.faith.databinding.PostCreateFragmentBinding
import com.example.faith.domain.Post
import com.example.faith.ui.home.HomeViewModelFactory

class PostCreateFragment : Fragment() {

    companion object {
        fun newInstance() = PostCreateFragment()
    }

    private lateinit var viewModel: PostCreateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<PostCreateFragmentBinding>(inflater,R.layout.post_create_fragment, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao
        val viewModelFactory = PostCreateViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PostCreateViewModel::class.java)

        binding.lifecycleOwner = this

        binding.createPostButton.setOnClickListener {
            insertDataToDatabase(binding)

        }

        return binding.root
    }

    private fun insertDataToDatabase(binding: PostCreateFragmentBinding) {
        val textValue = binding.createPostText.getText().toString()
        if (!textValue.isNullOrEmpty()) {
            val post = Post(0, binding.createPostText.getText().toString())
            viewModel.addPost(post)
        }
    }

}