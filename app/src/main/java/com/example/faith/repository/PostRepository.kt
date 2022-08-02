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

class PostRepository(private val database : FaithDatabase) {
    val posts = MediatorLiveData<List<Post>>()
    val filter = MutableLiveData<String>(null)
    val posts2 = Transformations.map(database.postDatabaseDao.getAllPosts()) {
        it.asDomainModel()
    }

    private var changeableLiveData = Transformations.map(database.postDatabaseDao.getAllPosts()) {
        it.asDomainModel()
    }

    init {
        posts.addSource(
            changeableLiveData
        ) {
            posts.setValue(it)
        }
    }

    fun addFilter(filter: String?) {
        this.filter.value = filter
    }

    suspend fun addPost(post: Post) {
        val databasePost = DatabasePost(post.postId, post.text)
        database.postDatabaseDao.insert(databasePost)
    }
}