package com.example.faith.ui.profile_edit

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.faith.database.FaithDatabase
import com.example.faith.database.user.UserDatabaseDao
import com.example.faith.domain.User
import com.example.faith.login.CredentialsManager
import com.example.faith.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileEditViewModel(val database: UserDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = UserRepository(db)
    val posts = repository.users

    fun editProfile(userName: String, image: Bitmap?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(CredentialsManager.getUserDetails()["userId"] as Long, userName, image)
        }
    }

}