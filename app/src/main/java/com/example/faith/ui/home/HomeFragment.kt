package com.example.faith.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.FragmentHomeBinding
import com.example.faith.domain.Post
import com.example.faith.login.CredentialsManager

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home, container, false)

        // Necessary starter things
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        setHasOptionsMenu(true)

        binding.lifecycleOwner = this

        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Home"

        // HomeViewModel & RecyclerView
        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        var recyclerView = binding.postList
        val adapter = HomeAdapter(PostListener {
                postId, operation ->
            if (operation == 1) {
                homeViewModel.onPostClicked(postId)
            } else if (operation == 2){
                homeViewModel.onPostDeleteClick(postId)
            } else {
                homeViewModel.onPostUpdateClick(postId)
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // For clicks on recyclerview
        homeViewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        homeViewModel.navigateToPostDetail.observe(viewLifecycleOwner, Observer {post ->
            post?.let {
                this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(post))
                homeViewModel.onPostNavigated()
            }
        })
        homeViewModel.canDeletePost.observe(viewLifecycleOwner, Observer {post ->
            post?.let {
                homeViewModel.deletePost(post)
                homeViewModel.onPostDeleted()
            }
        })
        homeViewModel.canEditPost.observe(viewLifecycleOwner, Observer {post ->
            post?.let {
                this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPostEditFragment(post))
                homeViewModel.onPostUpdated()
            }
        })

        binding.homeViewModel = homeViewModel

        // OnClickListeners
        binding.addPostButton.setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_postCreateFragment)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}