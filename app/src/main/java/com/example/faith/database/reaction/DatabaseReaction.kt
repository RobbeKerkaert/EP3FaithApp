package com.example.faith.database.reaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.faith.domain.Post
import com.example.faith.domain.Reaction

@Entity(tableName = "reaction_table")
data class DatabaseReaction(
    @PrimaryKey(autoGenerate = true)
    var reactionId: Long = 0L,

    @ColumnInfo(name = "postId")
    var postId: Long = 0L,

    @ColumnInfo(name = "userId")
    var userId: Long = 0L,

    @ColumnInfo(name = "text")
    var text: String = ""
)

fun List<DatabaseReaction>.asDomainModel() : List<Reaction> {
    return map {
        Reaction(
            reactionId = it.reactionId,
            postId = it.postId,
            userId = it.userId,
            text = it.text
        )
    }
}