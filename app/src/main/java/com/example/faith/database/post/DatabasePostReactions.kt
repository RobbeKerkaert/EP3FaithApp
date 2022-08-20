package com.example.faith.database.post

import androidx.room.Embedded
import androidx.room.Relation
import com.example.faith.database.reaction.DatabaseReaction

data class DatabasePostReactions(
    @Embedded var post: DatabasePost,
    @Relation(
        parentColumn = "postId",
        entityColumn = "postId"
    )
    var reactions: List<DatabaseReaction>
)
