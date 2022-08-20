package com.example.faith.ui.post_detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faith.database.post.PostDatabaseDao
import java.lang.IllegalArgumentException

class PostDetailViewModelFactory(
    private val postId: Long,
    private val postDataSource: PostDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostDetailViewModel::class.java)) {
            return PostDetailViewModel(postId, postDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
