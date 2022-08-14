package com.example.faith.ui.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faith.database.post.PostDatabaseDao
import java.lang.IllegalArgumentException

class ProfileViewModelFactory(
    private val postDataSource: PostDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(postDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}