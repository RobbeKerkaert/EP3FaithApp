package com.example.faith.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.domain.Post
import com.example.faith.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val database: PostDatabaseDao, application: Application): AndroidViewModel(application) {


    val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = PostRepository(db)
    val posts = repository.posts2

    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPost(post)
        }
    }
}