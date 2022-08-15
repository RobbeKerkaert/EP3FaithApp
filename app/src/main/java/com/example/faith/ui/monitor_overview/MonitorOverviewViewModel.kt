package com.example.faith.ui.monitor_overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.repository.PostRepository

class MonitorOverviewViewModel(val database: PostDatabaseDao, application: Application): AndroidViewModel(application) {

    // For database
    val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = PostRepository(db)
    val posts = repository.monitorPosts

    // For navigation
    private val _navigateToPostDetail = MutableLiveData<Long?>()
    val navigateToPostDetail
        get() = _navigateToPostDetail

    fun onPostClicked(postId: Long){
        _navigateToPostDetail.value = postId
    }
    fun onPostNavigated() {
        _navigateToPostDetail.value = null
    }

    // For navigation
    private val _changePostState = MutableLiveData<Long?>()
    val changePostState
        get() = _changePostState

    fun onPostStateClicked(postId: Long){
        _changePostState.value = postId
    }
    fun onPostStateChanged() {
        _changePostState.value = null
    }
}