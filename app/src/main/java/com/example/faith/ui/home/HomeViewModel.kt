package com.example.faith.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.faith.database.post.PostDatabaseDao

class HomeViewModel(val database: PostDatabaseDao, application: Application): AndroidViewModel(application) {

}