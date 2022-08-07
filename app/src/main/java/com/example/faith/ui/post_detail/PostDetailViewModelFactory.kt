package com.example.faith.ui.post_detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.ui.post_create.PostCreateViewModel
import java.lang.IllegalArgumentException

class PostDetailViewModelFactory(
    private val postId: Long,
    private val dataSource: PostDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostDetailViewModel::class.java)) {
            return PostDetailViewModel(postId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}