package com.example.faith.ui.post_detail

import android.app.Application
import androidx.lifecycle.*
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.DatabasePost
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.repository.PostRepository

class PostDetailViewModel(private val postKey: Long = 0L, dataSource: PostDatabaseDao) : ViewModel() {

    val db = dataSource

    private val databasePost = MediatorLiveData<DatabasePost>()

    fun getPost() = databasePost

    init {
        databasePost.addSource(db.getPostById(postKey), databasePost::setValue)
    }

    private val _navigateToHome = MutableLiveData<Boolean?>()

    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    fun doneNavigating() {
        _navigateToHome.value = null
    }

    fun onClose() {
        _navigateToHome.value = true
    }
}