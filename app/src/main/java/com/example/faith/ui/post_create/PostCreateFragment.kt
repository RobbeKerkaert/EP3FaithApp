package com.example.faith.ui.post_create

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import com.example.faith.domain.Post
import com.example.faith.domain.PostState
import com.example.faith.login.CredentialsManager

class PostCreateFragment : Fragment() {

    private lateinit var viewModel: PostCreateViewModel
    private lateinit var imageView: ImageView

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {

        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Create Post"

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<PostCreateFragmentBinding>(inflater,R.layout.post_create_fragment, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao
        val viewModelFactory = PostCreateViewModelFactory(dataSource, application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PostCreateViewModel::class.java)
        imageView = binding.pickImage

        binding.lifecycleOwner = this

        // Click listeners
        binding.pickImageButton.setOnClickListener {
            pickPostImage()
        }

        binding.createPostButton.setOnClickListener {
            insertDataToDatabase(binding)
            it.findNavController().navigate(R.id.action_postCreateFragment_to_homeFragment)
        }

        return binding.root
    }

    private  fun insertDataToDatabase(binding: PostCreateFragmentBinding) {
        val textValue = binding.createPostText.getText().toString()
        val imageUri: Bitmap? = if (imageView.drawable != null) {
            (imageView.drawable as BitmapDrawable).bitmap
        } else {
            null
        }
        val currentUserDetails = CredentialsManager.getUserDetails()

        if (!textValue.isNullOrEmpty()) {
            val post = Post(0, textValue, currentUserDetails["userName"] as String, currentUserDetails["userId"] as Long,
                    PostState.NEW, false, imageUri)
            viewModel.addPost(post)
        }
    }

    private fun pickPostImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            imageView.setImageURI(data?.data)
            imageView.tag = data?.data.toString()
        }
    }

}