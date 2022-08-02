package com.example.faith.database.post

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.faith.domain.Post

@Entity(tableName = "post_table")
data class DatabasePost(
    @PrimaryKey(autoGenerate = true)
    var postId: Long = 0L,

    @ColumnInfo(name = "text")
    var text: String = ""

)

fun List<DatabasePost>.asDomainModel() : List<Post> {
    return map {
        Post(
            postId = it.postId,
            text = it.text
        )
    }
}