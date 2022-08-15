package com.example.faith.domain

import com.example.faith.database.post.DatabasePost


data class Post(
    var postId: Long = 0L,
    var text: String = "",
    var userName: String = "",
    var userId: Long = 0L,
    var postState: PostState = PostState.NEW,
    var isFavorite: Boolean = false
)

fun List<Post>.asDatabaseModel() : List<DatabasePost> {
    return map {
        DatabasePost(
            postId = it.postId,
            text = it.text,
            userName = it.userName,
            userId = it.userId,
            postState = it.postState,
            isFavorite = it.isFavorite
        )
    }
}