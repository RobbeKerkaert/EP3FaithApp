package com.example.faith.repository

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.faith.database.FaithDatabase
import com.example.faith.database.user.DatabaseUser
import com.example.faith.database.user.asDomainModel
import com.example.faith.domain.Post
import com.example.faith.domain.User

class UserRepository(private val database : FaithDatabase) {
    val users = MediatorLiveData<List<User>>()

    private var changeableLiveData = Transformations.map(database.userDatabaseDao.getAllUsers()) {
        it.asDomainModel()
    }

    init {
        users.addSource(
            changeableLiveData
        ) {
            users.setValue(it)
        }
    }

    suspend fun getUserByEmail(email: String): User {
        val databaseUser = database.userDatabaseDao.getUserByEmail(email)
        val user = databaseUser?.let { User(it.userId, it.userName, it.email, it.isMonitor) }
        return user
    }
}