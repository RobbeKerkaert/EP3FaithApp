package com.example.faith.ui.monitor_overview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.MonitorOverviewFragmentBinding
import com.example.faith.domain.PostState

class MonitorOverviewFragment : Fragment() {


    private lateinit var viewModel: MonitorOverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<MonitorOverviewFragmentBinding>(inflater,
            R.layout.monitor_overview_fragment, container, false)

        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Post Overview"

        // Necessary starter things
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao
        val viewModelFactory = MonitorOverviewViewModelFactory(dataSource, application)

        binding.lifecycleOwner = this

        // HomeViewModel & RecyclerView
        viewModel = ViewModelProvider(this, viewModelFactory).get(MonitorOverviewViewModel::class.java)

        var recyclerView = binding.monitorPostList
        val adapter = MonitorPostAdapter(MonitorPostListener() {
                postId, operation ->
            if (operation == 1) {
                viewModel.onPostClicked(postId)
            } else {
                viewModel.onPostCloseClicked(postId)
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // For clicks on recyclerview
        viewModel.monitorPosts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.navigateToPostDetail.observe(viewLifecycleOwner, Observer {post ->
            post?.let {
                this.findNavController().navigate(MonitorOverviewFragmentDirections.actionMonitorOverviewFragmentToPostDetailFragment(post))
                viewModel.updatePostState(post, PostState.READ)
                viewModel.onPostNavigated()
            }
        })
        viewModel.changePostState.observe(viewLifecycleOwner, Observer {post ->
            post?.let {
                viewModel.updatePostState(post, PostState.ANSWERED)
                viewModel.onPostClosed()
            }
        })

        // OnClickListeners
        binding.newPostsButton.setOnClickListener {
            viewModel.movePostState(PostState.NEW)
        }
        binding.readPostsButton.setOnClickListener {
            viewModel.movePostState(PostState.READ)
        }
        binding.answeredPostsButton.setOnClickListener {
            viewModel.movePostState(PostState.ANSWERED)
        }

        binding.viewModel = viewModel

        return binding.root
    }

}