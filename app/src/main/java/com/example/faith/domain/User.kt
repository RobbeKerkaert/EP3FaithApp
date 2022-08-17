package com.example.faith.domain

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import com.example.faith.database.user.DatabaseUser

data class User(
    var userId: Long = 0L,
    var userName: String = "",
    var email: String = "",
    var isMonitor: Boolean = false,
    var image: Bitmap? = null
)

fun List<User>.asDatabaseModel() : List<DatabaseUser> {
    return map {
        DatabaseUser(
            userId = it.userId,
            userName = it.userName,
            email = it.email,
            isMonitor = it.isMonitor,
            image = it.image
        )
    }
}