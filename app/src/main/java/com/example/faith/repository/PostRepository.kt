package com.example.faith.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.DatabasePost
import com.example.faith.database.post.asDomainModel
import com.example.faith.domain.Post
import com.example.faith.domain.PostState
import com.example.faith.login.CredentialsManager

class PostRepository(private val database : FaithDatabase) {
    val posts = MediatorLiveData<List<Post>>()
    val favoritePosts = MediatorLiveData<List<Post>>()
    val monitorPosts = MediatorLiveData<List<Post>>()

    private var postsByUserId = Transformations.map(database.postDatabaseDao
        .getPostsByUserId(CredentialsManager.getUserDetails()["userId"] as Long)) {
        it.asDomainModel()
    }
    private var favoritePostsByUserId = Transformations.map(database.postDatabaseDao
        .getFavoritePostsByUserId(CredentialsManager.getUserDetails()["userId"] as Long)) {
        it.asDomainModel()
    }
    private var monitorPostsByPostState = Transformations.map(database.postDatabaseDao
        .getMonitorPostsByPostState(CredentialsManager.getUserDetails()["userIdList"] as List<Long>, PostState.NEW)) {
        it.asDomainModel()
    }

    init {
        posts.addSource(postsByUserId) {
            posts.setValue(it)
        }
        favoritePosts.addSource(favoritePostsByUserId) {
            favoritePosts.setValue(it)
        }

        monitorPosts.addSource(monitorPostsByPostState) {
            monitorPosts.setValue(it)
        }
    }

    suspend fun addPost(post: Post) {
        database.postDatabaseDao.insert(DatabasePost(post.postId, post.text, post.userName, post.userId))
    }

    suspend fun updatePost(postId: Long, newPost: Post) {
        val oldPost = getPost(postId)
        if (oldPost != null) {
            database.postDatabaseDao.update(DatabasePost(oldPost.postId, newPost.text, oldPost.userName, oldPost.userId, oldPost.postState, oldPost.isFavorite))
        }
    }

    suspend fun favoritePost(postId: Long) {
        val oldPost = getPost(postId)
        if (oldPost != null) {
            database.postDatabaseDao.update(DatabasePost(oldPost.postId, oldPost.text, oldPost.userName, oldPost.userId, oldPost.postState, !oldPost.isFavorite))
        }
    }

    suspend fun deletePost(postId: Long) {
        val post = getPost(postId)
        if (post != null) {
            database.postDatabaseDao.delete(DatabasePost(post.postId, post.text, post.userName, post.userId))
        }
    }

    suspend fun getPost(postId: Long): Post? {
        val databasePost = database.postDatabaseDao.get(postId)
        val post = databasePost?.let { Post(it.postId, it.text, it.userName, it.userId, it.postState, it.isFavorite) }
        return post
    }

    suspend fun getPostsByPostState(userIdList: List<Long>, postState: PostState): LiveData<List<Post>> {
        val databasePostList = database.postDatabaseDao.getMonitorPostsByPostState(userIdList, postState)
        return databasePostList as LiveData<List<Post>>
    }
}