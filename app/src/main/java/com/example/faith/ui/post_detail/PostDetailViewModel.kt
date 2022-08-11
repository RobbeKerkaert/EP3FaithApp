package com.example.faith.ui.post_detail

import android.app.Application
import androidx.lifecycle.*
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.DatabasePost
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.database.reaction.DatabaseReaction
import com.example.faith.database.reaction.ReactionDatabaseDao
import com.example.faith.domain.Post
import com.example.faith.domain.Reaction
import com.example.faith.repository.PostRepository
import com.example.faith.repository.ReactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostDetailViewModel(private val postKey: Long = 0L, postDataSource: PostDatabaseDao, reactionDataSource: ReactionDatabaseDao,
                          application: Application) : ViewModel() {
    private val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = ReactionRepository(db, postKey)

    private val dbPosts = postDataSource
    private val dbReactions = reactionDataSource
    private val databasePost = MediatorLiveData<DatabasePost>()

    val reactions = repository.reactions

    fun getPost() = databasePost

    init {
        databasePost.addSource(dbPosts.getPostById(postKey), databasePost::setValue)
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

    // For deleting
    private val _canDeleteReaction = MutableLiveData<Long?>()
    val canDeleteReaction
        get() = _canDeleteReaction

    fun onReactionDeleteClick(reactionId: Long) {
        _canDeleteReaction.value = reactionId
    }
    fun onReactionDeleted() {
        _canDeleteReaction.value = null
    }

    // Other functions
    fun addReaction(reaction: Reaction) {
        reaction.postId = postKey
        viewModelScope.launch(Dispatchers.IO) {
            repository.addReaction(reaction)
        }
    }

    fun deleteReaction(reactionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReaction(reactionId)
        }
    }


}