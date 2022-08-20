package com.example.faith.database.reaction

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReactionDatabaseDao {
    /**
     * Creates a new reaction
     */
    @Insert
    fun insert(reaction: DatabaseReaction)

    /**
     * Updates a reaction with new info, after an edit.
     */
    @Update
    fun update(reaction: DatabaseReaction)

    /**
     * Deletes reaction with reactionId from table
     */
    @Delete
    fun delete(reaction: DatabaseReaction)

    /**
     * Gets a specific reaction with the matching key.
     */
    @Query("SELECT * from reaction_table WHERE reactionId = :key")
    fun get(key: Long): DatabaseReaction?

    /**
     * Selects and returns the reactions of a post with a given id as LiveData
     */
    @Query("SELECT * from reaction_table WHERE postId = :key")
    fun getReactionsById(key: Long): LiveData<List<DatabaseReaction>>

    /**
     * Returns the amount of rows within the post table
     */
    @Query("SELECT COUNT(reactionId) FROM reaction_table")
    fun getCount(): Int
}
