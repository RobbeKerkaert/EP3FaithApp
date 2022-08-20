package com.example.faith.repository

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.faith.database.FaithDatabase
import com.example.faith.database.reaction.DatabaseReaction
import com.example.faith.database.reaction.asDomainModel
import com.example.faith.domain.Reaction

class ReactionRepository(private val database: FaithDatabase, private val postKey: Long) {
    val reactions = MediatorLiveData<List<Reaction>>()

    private var changeableLiveData = Transformations.map(database.reactionDatabaseDao.getReactionsById(postKey)) {
        it.asDomainModel()
    }

    init {
        reactions.addSource(
            changeableLiveData
        ) {
            reactions.setValue(it)
        }
    }

    suspend fun addReaction(reaction: Reaction) {
        database.reactionDatabaseDao.insert(
            DatabaseReaction(
                reaction.reactionId,
                reaction.postId,
                reaction.userId,
                reaction.text,
                reaction.userName
            )
        )
    }

    suspend fun updateReaction(reactionId: Long, newReaction: Reaction) {
        val oldReaction = getReaction(reactionId)
        if (oldReaction != null) {
            database.reactionDatabaseDao.update(
                DatabaseReaction(
                    oldReaction.reactionId,
                    oldReaction.postId,
                    oldReaction.userId,
                    newReaction.text,
                    oldReaction.userName
                )
            )
        }
    }

    suspend fun deleteReaction(reactionId: Long) {
        val reaction = getReaction(reactionId)
        if (reaction != null) {
            database.reactionDatabaseDao.delete(
                DatabaseReaction(
                    reaction.reactionId,
                    reaction.postId,
                    reaction.userId,
                    reaction.text,
                    reaction.userName
                )
            )
        }
    }

    suspend fun getReaction(reactionId: Long): Reaction? {
        val databaseReaction = database.reactionDatabaseDao.get(reactionId)
        val reaction = databaseReaction?.let {
            Reaction(
                it.reactionId,
                it.postId,
                it.userId,
                it.text,
                it.userName
            )
        }
        return reaction
    }
}
