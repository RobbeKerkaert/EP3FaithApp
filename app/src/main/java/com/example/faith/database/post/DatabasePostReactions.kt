package com.example.faith.database.post

import androidx.lifecycle.Transformations.map
import androidx.room.Embedded
import androidx.room.Relation
import com.example.faith.database.reaction.DatabaseReaction
import com.example.faith.domain.Post
import com.example.faith.domain.PostReactions

data class DatabasePostReactions(
    @Embedded var post: DatabasePost,
    @Relation(
        parentColumn = "postId",
        entityColumn = "postId"
    )
    var reactions: List<DatabaseReaction>
)