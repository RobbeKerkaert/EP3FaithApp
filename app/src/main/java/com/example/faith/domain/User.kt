package com.example.faith.domain

import com.example.faith.database.user.DatabaseUser

data class User(
    var userId: Long = 0L,
    var userName: String = "",
    var email: String = "",
    var isMonitor: Boolean = false
)

fun List<User>.asDatabaseModel() : List<DatabaseUser> {
    return map {
        DatabaseUser(
            userId = it.userId,
            userName = it.userName,
            email = it.email,
            isMonitor = it.isMonitor
        )
    }
}