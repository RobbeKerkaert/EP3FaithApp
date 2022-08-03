package com.example.faith.ui.profile_edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.faith.R

class ProfileEditFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileEditFragment()
    }

    private lateinit var viewModel: ProfileEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_edit_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileEditViewModel::class.java)
        // TODO: Use the ViewModel
    }

}