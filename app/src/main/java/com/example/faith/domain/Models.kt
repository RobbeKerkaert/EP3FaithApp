package com.example.faith.domain

data class Post(
    var postId: Long = 0L,
    var text: String = "",
    var userName: String = ""
)

data class User(
    var userName: String = ""
)

data class Reaction(
    var reactionId: Long = 0L,
    var postId: Long = 0L,
    var text: String = ""
)