package com.example.faith.database.user

import androidx.room.Embedded
import androidx.room.Relation
import com.example.faith.database.post.DatabasePost

data class DatabaseUserPosts(
    @Embedded
    var user: DatabaseUser,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    var posts: List<DatabasePost>
)