package com.example.faith.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.domain.Post
import com.example.faith.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val database: PostDatabaseDao, application: Application): AndroidViewModel(application) {

    // For action bar title
    private val _title = MutableLiveData<String>()
    val title: LiveData<String>
        get() = _title

    // For database
    val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = PostRepository(db)
    val posts = repository.posts2

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

    // For deleting
    private val _canDeletePost = MutableLiveData<Long?>()
    val canDeletePost
        get() = _canDeletePost

    fun onPostDeleteClick(postId: Long) {
        _canDeletePost.value = postId
    }
    fun onPostDeleted() {
        _canDeletePost.value = null
    }

    // Functions
    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPost(post)
        }
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePost(postId)
        }
    }

    fun updateActionBarTitle(title: String) = _title.postValue(title)
}