package com.example.faith.ui.post_edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.domain.Post
import com.example.faith.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostEditViewModel(private val postKey: Long = 0L, val database: PostDatabaseDao,
                        application: Application) : ViewModel() {
    val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = PostRepository(db)

    fun editPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePost(postKey, post)
        }
    }
}