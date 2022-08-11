package com.example.faith.ui.reaction_edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faith.database.FaithDatabase
import com.example.faith.database.reaction.ReactionDatabaseDao
import com.example.faith.domain.Reaction
import com.example.faith.repository.ReactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReactionEditViewModel(private val reactionKey: Long = 0L, val database: ReactionDatabaseDao,
                            application: Application
) : ViewModel() {
    val db = FaithDatabase.getInstance(application.applicationContext)
    private val repository = ReactionRepository(db, 0)

    fun editReaction(reaction: Reaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReaction(reactionKey, reaction)
        }
    }
}