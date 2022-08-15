package com.example.faith.database.post

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.faith.domain.PostState

@Dao
interface PostDatabaseDao {
    /**
     * Creates a new post
     */
    @Insert
    suspend fun insert(post: DatabasePost)

    /**
     * Updates a post with new info, after an edit.
     */
    @Update
    suspend fun update(post: DatabasePost)

    /**
     * Deletes post with postId from table
     */
    @Delete
    suspend fun delete(post: DatabasePost)

    /**
     * Clears all posts from table
     */
    @Query("DELETE FROM post_table")
    suspend fun clear()

    /**
     * Gets a specific post with the matching key.
     */
    @Query("SELECT * from post_table WHERE postId = :key")
    suspend fun get(key: Long): DatabasePost?

    /**
     * Selects and returns the post with a given id as LiveData
     */
    @Query("SELECT * from post_table WHERE postId = :key")
    fun getPostByPostId(key: Long): LiveData<DatabasePost>

    /**
     * Selects and returns all rows in the table.
     * sorted by id in descending order. Should probably get something else for this.
     */
    @Query("SELECT * FROM post_table ORDER BY postId DESC")
    fun getAllPosts(): LiveData<List<DatabasePost>>

    /**
     * Selects and returns all posts with a given userId as LiveData
     */
    @Query("SELECT * from post_table WHERE userId = :key")
    fun getPostsByUserId(key: Long): LiveData<List<DatabasePost>>

    /**
     * Selects and returns all favorited posts with a given userId as LiveData
     */
    @Query("SELECT * from post_table WHERE userId = :key AND favorited = ${true}")
    fun getFavoritePostsByUserId(key: Long): LiveData<List<DatabasePost>>

    /**
     * Selects and returns posts given a certain postState
     */
    @Query("SELECT * from post_table WHERE userId IN (:userIdList) AND postState = :postState")
    fun getMonitorPostsByPostState(userIdList: List<Long>, postState: PostState): LiveData<List<DatabasePost>>

    @Transaction
    @Query("SELECT * FROM post_table")
    fun getPostsWithReactions(): List<DatabasePostReactions>
}