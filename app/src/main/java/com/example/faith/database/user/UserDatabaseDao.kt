package com.example.faith.database.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDatabaseDao {
    /**
     * Creates a new user
     */
    @Insert
    suspend fun insert(user: DatabaseUser)

    /**
     * Updates a user with new info, after an edit.
     */
    @Update
    suspend fun update(user: DatabaseUser)

    /**
     * Gets a specific user with the matching key.
     */
    @Query("SELECT * from user_table WHERE userName = :key")
    suspend fun get(key: String): DatabaseUser?

    /**
     * Selects and returns all rows in the table.
     * sorted by name in descending order.
     */
    @Query("SELECT * FROM user_table ORDER BY userName DESC")
    fun getAllUsers(): LiveData<List<DatabaseUser>>
}