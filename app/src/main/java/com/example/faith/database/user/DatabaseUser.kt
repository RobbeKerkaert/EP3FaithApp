package com.example.faith.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.faith.domain.User

@Entity(tableName = "user_table")
data class DatabaseUser(

    @PrimaryKey(autoGenerate = false)
    var userName: String = ""

)

fun List<DatabaseUser>.asDomainModel() : List<User> {
    return map {
        User(
            userName = it.userName
        )
    }
}