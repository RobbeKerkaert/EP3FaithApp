package com.example.faith.database.post

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PostDatabaseDao {
    /**
     * Creates a new post
     */
    @Insert
    suspend fun insert(post: Post)

    /**
     * Updates a post with new info, after an edit.
     */
    @Update
    suspend fun update(post: Post)

    /**
     * Gets a specific post with the matching key.
     */
    @Query("SELECT * from post_table WHERE postId = :key")
    suspend fun get(key: Long): Post?

    /**
     * Selects and returns all rows in the table.
     * sorted by id in descending order. Should probably get something else for this.
     */
    @Query("SELECT * FROM post_table ORDER BY postId DESC")
    fun getAllPosts(): LiveData<List<Post>>
}