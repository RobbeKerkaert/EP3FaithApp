package com.example.faith.ui.profile_edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.wifi.hotspot2.pps.Credential
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.PostCreateFragmentBinding
import com.example.faith.databinding.ProfileEditFragmentBinding
import com.example.faith.login.CredentialsManager
import com.example.faith.ui.post_create.PostCreateFragment
import com.example.faith.ui.post_create.PostCreateViewModel
import com.example.faith.ui.post_create.PostCreateViewModelFactory

class ProfileEditFragment : Fragment() {

    private lateinit var viewModel: ProfileEditViewModel
    private lateinit var imageView: ImageView

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Edit your profile"

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<ProfileEditFragmentBinding>(inflater,R.layout.profile_edit_fragment, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = ProfileEditViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ProfileEditViewModel::class.java)
        imageView = binding.pickImage

        binding.lifecycleOwner = this

        // Click listeners
        binding.pickImageButton.setOnClickListener {
            pickProfileImage()
        }

        binding.editProfileButton.setOnClickListener {
            var newUsername = binding.editProfileUserName.getText().toString()
            val image: Bitmap? = if (imageView.drawable != null) { (imageView.drawable as BitmapDrawable).bitmap } else {
                null
            }
            if (!newUsername.isNullOrEmpty()) {
                viewModel.editProfile(newUsername, image)
                var userDetails = CredentialsManager.getUserDetails()
                userDetails["userName"] = newUsername
                CredentialsManager.setUserDetails(userDetails)
            }
            it.findNavController().navigate(R.id.action_profileEditFragment_to_profileFragment)
        }

        return binding.root
    }

    private fun pickProfileImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PostCreateFragment.IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PostCreateFragment.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(data?.data)
            imageView.tag = data?.data.toString()
        }
    }

}