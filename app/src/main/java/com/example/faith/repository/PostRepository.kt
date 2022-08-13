package com.example.faith.repository

import android.provider.ContactsContract
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.Database
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.DatabasePost
import com.example.faith.database.post.asDomainModel
import com.example.faith.domain.Post
import com.example.faith.login.CredentialsManager

class PostRepository(private val database : FaithDatabase) {
    val posts = MediatorLiveData<List<Post>>()

    private var changeableLiveData = Transformations.map(database.postDatabaseDao
        .getPostsByUserId(CredentialsManager.getUserDetails()["userId"] as Long)) {
        it.asDomainModel()
    }

    init {
        posts.addSource(
            changeableLiveData
        ) {
            posts.setValue(it)
        }
    }

    suspend fun addPost(post: Post) {
        database.postDatabaseDao.insert(DatabasePost(post.postId, post.text, post.userName, post.userId))
    }

    suspend fun updatePost(postId: Long, newPost: Post) {
        val oldPost = getPost(postId)
        if (oldPost != null) {
            database.postDatabaseDao.update(DatabasePost(oldPost.postId, newPost.text, oldPost.userName, oldPost.userId))
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
        val post = databasePost?.let { Post(it.postId, it.text, it.userName, it.userId) }
        return post
    }
}