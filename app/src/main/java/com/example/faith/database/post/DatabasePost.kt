package com.example.faith.database.post

import androidx.room.*
import com.example.faith.database.reaction.DatabaseReaction
import com.example.faith.database.user.DatabaseUser
import com.example.faith.domain.Post

@Entity(tableName = "post_table")
data class DatabasePost(
    @PrimaryKey(autoGenerate = true)
    var postId: Long = 0L,

    @ColumnInfo(name = "text")
    var text: String = "",

    @ColumnInfo(name = "userName")
    var userName: String = ""
)

fun List<DatabasePost>.asDomainModel() : List<Post> {
    return map {
        Post(
            postId = it.postId,
            text = it.text,
            userName = it.userName
        )
    }
}