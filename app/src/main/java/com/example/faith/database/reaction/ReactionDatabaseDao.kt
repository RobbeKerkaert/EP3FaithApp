package com.example.faith.database.reaction

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.faith.database.post.DatabasePost

@Dao
interface ReactionDatabaseDao {
    /**
     * Creates a new reaction
     */
    @Insert
    suspend fun insert(reaction: DatabaseReaction)

    /**
     * Updates a reaction with new info, after an edit.
     */
    @Update
    suspend fun update(reaction: DatabaseReaction)

    /**
     * Deletes reaction with reactionId from table
     */
    @Delete
    suspend fun delete(reaction: DatabaseReaction)

    /**
     * Gets a specific reaction with the matching key.
     */
    @Query("SELECT * from reaction_table WHERE reactionId = :key")
    suspend fun get(key: Long): DatabaseReaction?

    /**
     * Selects and returns all rows in the table.
     * sorted by id in descending order.
     */
    @Query("SELECT * FROM reaction_table ORDER BY reactionId DESC")
    fun getAllReactions(): LiveData<List<DatabaseReaction>>

    /**
     * Selects and returns the reactions of a post with a given id as LiveData
     */
    @Query("SELECT * from reaction_table WHERE postId = :key")
    fun getReactionsById(key: Long): LiveData<List<DatabaseReaction>>
}