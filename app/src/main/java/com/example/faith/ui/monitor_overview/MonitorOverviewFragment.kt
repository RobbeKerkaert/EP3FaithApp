package com.example.faith.ui.monitor_overview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.FragmentHomeBinding
import com.example.faith.databinding.MonitorOverviewFragmentBinding
import com.example.faith.ui.home.*

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
            viewModel.onPostClicked(postId)
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // For clicks on recyclerview
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.navigateToPostDetail.observe(viewLifecycleOwner, Observer {post ->
            post?.let {
                this.findNavController().navigate(MonitorOverviewFragmentDirections.actionMonitorOverviewFragmentToPostDetailFragment(post))
                viewModel.onPostNavigated()
            }
        })

        binding.viewModel = viewModel

        return binding.root
    }

}