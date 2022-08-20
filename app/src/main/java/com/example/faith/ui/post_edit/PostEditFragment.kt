package com.example.faith.ui.post_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.PostEditFragmentBinding
import com.example.faith.domain.Post

class PostEditFragment : Fragment() {

    private lateinit var viewModel: PostEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Post Edit"

        // Necessary starter things
        val binding = DataBindingUtil.inflate<PostEditFragmentBinding>(
            inflater,
            R.layout.post_edit_fragment,
            container,
            false
        )

        val arguments = PostEditFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application
        val postDataSource = FaithDatabase.getInstance(application).postDatabaseDao

        val viewModelFactory = PostEditViewModelFactory(arguments.postId, postDataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostEditViewModel::class.java)

        // OnClickListeners
        binding.submitEditButton.setOnClickListener {
            editData(binding)
            it.findNavController().navigate(R.id.action_postEditFragment_to_homeFragment)
        }

        return binding.root
    }

    private fun editData(binding: PostEditFragmentBinding) {
        val textValue = binding.editPostText.getText().toString()

        if (!textValue.isNullOrEmpty()) {
            val post = Post(0, textValue, "")
            viewModel.editPost(post)
        }
    }
}
