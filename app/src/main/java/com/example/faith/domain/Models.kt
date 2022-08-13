package com.example.faith.domain

data class Post(
    var postId: Long = 0L,
    var text: String = "",
    var userName: String = "",
    var userId: Long = 0L,
    var isFavorite: Boolean = false
)

data class User(
    var userId: Long = 0L,
    var userName: String = "",
    var email: String = ""
)

data class Reaction(
    var reactionId: Long = 0L,
    var postId: Long = 0L,
    var userId: Long = 0L,
    var text: String = "",
    var userName: String = ""
)