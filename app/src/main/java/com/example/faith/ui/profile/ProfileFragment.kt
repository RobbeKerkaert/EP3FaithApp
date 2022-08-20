package com.example.faith.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.ProfileFragmentBinding
import com.example.faith.ui.home.PostAdapter
import com.example.faith.ui.home.PostListener

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Your Profile"
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<ProfileFragmentBinding>(
            inflater,
            R.layout.profile_fragment,
            container,
            false
        )

        // Necessary starter things
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = ProfileViewModelFactory(dataSource, application)

        binding.lifecycleOwner = this

        // HomeViewModel & RecyclerView
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)

        var recyclerView = binding.favoritesList
        val adapter = PostAdapter(
            PostListener { postId, operation ->
                if (operation == 1) {
                    viewModel.onPostClicked(postId)
                } else if (operation == 2) {
                    viewModel.onPostDeleteClick(postId)
                } else if (operation == 3) {
                    viewModel.onPostUpdateClick(postId)
                } else {
                    viewModel.onPostFavoriteClick(postId)
                }
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // For clicks on recyclerview
        viewModel.posts.observe(
            viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
            }
        )
        viewModel.navigateToPostDetail.observe(
            viewLifecycleOwner,
            Observer { post ->
                post?.let {
                    this.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPostDetailFragment(post))
                    viewModel.onPostNavigated()
                }
            }
        )
        viewModel.canDeletePost.observe(
            viewLifecycleOwner,
            Observer { post ->
                post?.let {
                    viewModel.deletePost(post)
                    viewModel.onPostDeleted()
                }
            }
        )
        viewModel.canEditPost.observe(
            viewLifecycleOwner,
            Observer { post ->
                post?.let {
                    this.findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPostEditFragment(post))
                    viewModel.onPostUpdated()
                }
            }
        )
        viewModel.canFavoritePost.observe(
            viewLifecycleOwner,
            Observer { post ->
                post?.let {
                    viewModel.favoritePost(post)
                    viewModel.onPostFavorited()
                }
            }
        )

        // Click listeners
        binding.toEditProfileButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_profileFragment_to_profileEditFragment)
        }

        binding.viewModel = viewModel

        return binding.root
    }
}
