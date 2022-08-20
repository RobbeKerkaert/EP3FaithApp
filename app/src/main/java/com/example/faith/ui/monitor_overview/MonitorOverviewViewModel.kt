package com.example.faith.ui.monitor_overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.database.post.asDomainModel
import com.example.faith.domain.PostState
import com.example.faith.login.CredentialsManager
import com.example.faith.repository.PostRepository
import kotlinx.coroutines.launch

class MonitorOverviewViewModel(val database: PostDatabaseDao, application: Application) : AndroidViewModel(application) {

    // For database
    val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = PostRepository(db)
    var monitorPosts = repository.monitorPosts

    // For navigation
    private val _navigateToPostDetail = MutableLiveData<Long?>()
    val navigateToPostDetail
        get() = _navigateToPostDetail

    fun onPostClicked(postId: Long) {
        _navigateToPostDetail.value = postId
    }
    fun onPostNavigated() {
        _navigateToPostDetail.value = null
    }

    fun movePostState(state: PostState) {
        var monitorPostsByPostState = Transformations.map(
            db.postDatabaseDao
                .getMonitorPostsByPostState(
                    CredentialsManager.getUserDetails()["userIdList"] as List<Long>,
                    state
                )
        ) {
            it.asDomainModel()
        }
        monitorPosts.addSource(monitorPostsByPostState) {
            monitorPosts.setValue(it)
        }
    }

    // For changing state of a post
    private val _changePostState = MutableLiveData<Long?>()
    val changePostState
        get() = _changePostState

    fun onPostCloseClicked(postId: Long) {
        _changePostState.value = postId
    }
    fun onPostClosed() {
        _changePostState.value = null
    }

    // Other functions
    fun updatePostState(postId: Long, postState: PostState) {
        viewModelScope.launch {
            repository.updatePostState(postId, postState)
        }
    }
}
