package com.example.faith.database.post

import androidx.room.Embedded
import androidx.room.Relation
import com.example.faith.database.reaction.DatabaseReaction
import com.example.faith.domain.Post

data class DatabasePostReactions(
    @Embedded var post: Post,
    @Relation(
        parentColumn = "postId",
        entityColumn = "postId"
    )
    var reactions: List<DatabaseReaction>
)