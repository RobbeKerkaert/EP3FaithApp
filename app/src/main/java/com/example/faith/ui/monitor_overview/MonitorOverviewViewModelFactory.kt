package com.example.faith.ui.monitor_overview

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

class MonitorOverviewViewModelFactory(
    private val dataSource: PostDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonitorOverviewViewModel::class.java)) {
            return MonitorOverviewViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}