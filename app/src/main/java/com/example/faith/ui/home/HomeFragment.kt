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
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.FragmentHomeBinding
import com.example.faith.domain.Post

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

        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        setHasOptionsMenu(true)

        // RecyclerView
        var recyclerView = binding.postList
        val adapter = HomeAdapter(PostListener { })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // HomeViewModel
        binding.lifecycleOwner = this

        homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        homeViewModel.posts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding.homeViewModel = homeViewModel

        // OnClickListeners
        binding.testAddButton.setOnClickListener {
            insertDataToDatabase()
        }
        binding.addPostButton.setOnClickListener { view:View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_postCreateFragment)
        }

        return binding.root
    }

    private fun insertDataToDatabase () {
        val text = "I AM A TEST"

        val post = Post(0, text)
        homeViewModel.addPost(post)
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