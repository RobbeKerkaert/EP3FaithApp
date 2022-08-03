package com.example.faith.ui.post_create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

class PostCreateViewModelFactory(
    private val dataSource: PostDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostCreateViewModel::class.java)) {
            return PostCreateViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}