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
import com.auth0.android.Auth0
import com.auth0.android.callback.Callback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.result.UserProfile
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.FragmentLoginBinding
import com.example.faith.databinding.PostCreateFragmentBinding
import com.example.faith.databinding.ProfileEditFragmentBinding
import com.example.faith.login.CredentialsManager
import com.example.faith.ui.post_create.PostCreateFragment
import com.example.faith.ui.post_create.PostCreateViewModel
import com.example.faith.ui.post_create.PostCreateViewModelFactory
import com.google.android.material.snackbar.Snackbar

class ProfileEditFragment : Fragment() {

    private lateinit var binding: ProfileEditFragmentBinding
    private lateinit var viewModel: ProfileEditViewModel
    private lateinit var imageView: ImageView
    private lateinit var account: Auth0

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Edit your profile"

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<ProfileEditFragmentBinding>(inflater,R.layout.profile_edit_fragment, container, false)
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
                setUserMetadata(newUsername)
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

    private fun setUserMetadata(userName: String) {
        // Guard against getting the metadata when no user is logged in
        if (CredentialsManager.cachedCredentials == null) {
            return
        }

        val usersClient = UsersAPIClient(account, CredentialsManager.cachedCredentials!!.accessToken!!)

        CredentialsManager.currentUserDetails["userName"] = userName

        usersClient
            .updateMetadata(CredentialsManager.cachedUserProfile!!.getId()!!, CredentialsManager.currentUserDetails)
            .start(object : Callback<UserProfile, ManagementException> {

                override fun onFailure(exception: ManagementException) {
//                    showSnackBar(getString(R.string.general_failure_with_exception_code,
//                        exception.getCode()))
                }

                override fun onSuccess(profile: UserProfile) {
                    CredentialsManager.cachedUserProfile = profile

//                    showSnackBar(getString(R.string.general_success_message))
                }

            })
    }

    private fun showSnackBar(text: String) {
        Snackbar
            .make(
                binding.root,
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }

}