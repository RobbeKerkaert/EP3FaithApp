package com.example.faith.domain

import com.example.faith.database.reaction.DatabaseReaction

data class Reaction(
    var reactionId: Long = 0L,
    var postId: Long = 0L,
    var userId: Long = 0L,
    var text: String = "",
    var userName: String = ""
)

fun List<Reaction>.asDatabaseModel() : List<DatabaseReaction> {
    return map {
        DatabaseReaction(
            reactionId = it.reactionId,
            postId = it.postId,
            userId = it.userId,
            text = it.text,
            userName = it.userName

        )
    }
}