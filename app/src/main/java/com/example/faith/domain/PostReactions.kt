package com.example.faith.domain


data class PostReactions(
    var post: Post,
    var reactions: List<Reaction>
)