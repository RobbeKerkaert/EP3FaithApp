package com.example.faith.ui.post_edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faith.database.post.PostDatabaseDao
import java.lang.IllegalArgumentException

class PostEditViewModelFactory(
    private val postId: Long,
    private val postDataSource: PostDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostEditViewModel::class.java)) {
            return PostEditViewModel(postId, postDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
