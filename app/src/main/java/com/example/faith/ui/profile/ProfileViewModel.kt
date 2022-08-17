package com.example.faith.ui.profile

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.DatabasePost
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.database.user.DatabaseUser
import com.example.faith.database.user.UserDatabaseDao
import com.example.faith.login.CredentialsManager
import com.example.faith.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(val database: UserDatabaseDao, application: Application) : ViewModel() {
    // For database
    val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = PostRepository(db)
    private val userDb = database
    val posts = repository.favoritePosts

    private val databaseUser = MediatorLiveData<DatabaseUser>()
    var user = databaseUser

    init {
        databaseUser.addSource(userDb.getUserById(CredentialsManager.getUserId()), databaseUser::setValue)
    }

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

    // For editing
    private val _canEditPost = MutableLiveData<Long?>()
    val canEditPost
        get() = _canEditPost

    fun onPostUpdateClick(postId: Long) {
        _canEditPost.value = postId
    }
    fun onPostUpdated() {
        _canEditPost.value = null
    }

    // For favoriting
    private val _canFavoritePost = MutableLiveData<Long?>()
    val canFavoritePost
        get() = _canFavoritePost

    fun onPostFavoriteClick(postId: Long) {
        _canFavoritePost.value = postId
    }
    fun onPostFavorited() {
        _canFavoritePost.value = null
    }

    // Functions
    fun deletePost(postId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePost(postId)
        }
    }

    fun favoritePost(postId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.favoritePost(postId)
        }
    }
}