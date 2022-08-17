package com.example.faith.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.faith.database.FaithDatabase
import com.example.faith.database.user.UserDatabaseDao
import com.example.faith.domain.User
import com.example.faith.repository.PostRepository
import com.example.faith.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(val database: UserDatabaseDao, application: Application): AndroidViewModel(application) {
    // For database
    val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = UserRepository(db)
    val users = repository.users
}