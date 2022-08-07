package com.example.faith.ui.monitor_overview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.faith.R

class MonitorOverviewFragment : Fragment() {

    companion object {
        fun newInstance() = MonitorOverviewFragment()
    }

    private lateinit var viewModel: MonitorOverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.monitor_overview_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MonitorOverviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}