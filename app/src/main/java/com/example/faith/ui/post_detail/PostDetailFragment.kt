package com.example.faith.ui.post_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.PostDetailFragmentBinding
import com.example.faith.domain.Reaction
import com.example.faith.login.CredentialsManager

class PostDetailFragment : Fragment() {

    private lateinit var viewModel: PostDetailViewModel
    private lateinit var binding: PostDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Post Reactions"

        // Necessary starter things
        binding =
            DataBindingUtil.inflate(inflater, R.layout.post_detail_fragment, container, false)

        val arguments = PostDetailFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application
        val postDataSource = FaithDatabase.getInstance(application).postDatabaseDao

        val viewModelFactory = PostDetailViewModelFactory(arguments.postId, postDataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostDetailViewModel::class.java)

        val recyclerView = binding.reactionList

        val adapter = ReactionAdapter(
            ReactionListener { reactionId, operation ->
                if (operation == 1) {
                    viewModel.onReactionUpdateClick(reactionId)
                } else {
                    viewModel.onReactionDeleteClick(reactionId)
                }
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // OnClickListeners
        binding.addReactionButton.setOnClickListener {
            var textValue = binding.reactionText.getText()
            if (!textValue.isNullOrEmpty()) {
                var currentUserDetails = CredentialsManager.getUserDetails()
                var reaction = Reaction(0, 0, CredentialsManager.getUserId(), textValue.toString(), currentUserDetails["userName"] as String)
                viewModel.addReaction(reaction)
                binding.reactionText.setText("")
            }
            binding.postImage.setImageBitmap(viewModel.post.value?.image)
        }

        viewModel.reactions.observe(
            viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
            }
        )

        viewModel.canEditReaction.observe(
            viewLifecycleOwner,
            Observer { reaction ->
                reaction?.let {
                    this.findNavController().navigate(
                        PostDetailFragmentDirections
                            .actionPostDetailFragmentToReactionEditFragment(arguments.postId, reaction)
                    )
                    viewModel.onReactionUpdated()
                }
            }
        )
        viewModel.canDeleteReaction.observe(
            viewLifecycleOwner,
            Observer { reaction ->
                reaction?.let {
                    viewModel.deleteReaction(reaction)
                    viewModel.onReactionDeleted()
                }
            }
        )

        binding.postDetailViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}
