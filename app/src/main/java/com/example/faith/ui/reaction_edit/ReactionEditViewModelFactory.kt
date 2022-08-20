package com.example.faith.ui.reaction_edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.faith.database.reaction.ReactionDatabaseDao
import java.lang.IllegalArgumentException

class ReactionEditViewModelFactory(
    private val reactionId: Long,
    private val reactionDataSource: ReactionDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReactionEditViewModel::class.java)) {
            return ReactionEditViewModel(reactionId, reactionDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
