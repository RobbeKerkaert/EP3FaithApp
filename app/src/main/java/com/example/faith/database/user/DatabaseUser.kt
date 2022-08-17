package com.example.faith.database.user

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.faith.database.post.DatabasePost
import com.example.faith.domain.User

@Entity(tableName = "user_table")
data class DatabaseUser(

    @PrimaryKey(autoGenerate = false)
    var userId: Long = 0L,
    @ColumnInfo(name = "userName")
    var userName: String = "",
    @ColumnInfo(name = "email")
    var email: String = "",
    @ColumnInfo(name = "isMonitor")
    var isMonitor: Boolean = false,
    @ColumnInfo(name = "imageUri")
    var image: Bitmap? = null
)

fun List<DatabaseUser>.asDomainModel() : List<User> {
    return map {
        User(
            userId = it.userId,
            userName = it.userName,
            email = it.email,
            isMonitor = it.isMonitor,
            image = it.image
        )
    }
}