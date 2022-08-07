package com.example.faith.ui.post_detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.PostDetailFragmentBinding

class PostDetailFragment : Fragment() {

    companion object {
        fun newInstance() = PostDetailFragment()
    }

    private lateinit var viewModel: PostDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Post Reactions"

        val binding: PostDetailFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.post_detail_fragment, container, false)

        val arguments = PostDetailFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao

        val viewModelFactory = PostDetailViewModelFactory(arguments.postId, dataSource)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(PostDetailViewModel::class.java)

        binding.postDetailViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

}