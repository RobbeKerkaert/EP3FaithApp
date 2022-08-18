package com.example.faith.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.faith.database.post.DatabasePost
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.database.reaction.DatabaseReaction
import com.example.faith.database.reaction.ReactionDatabaseDao
import com.example.faith.database.user.DatabaseUser
import com.example.faith.database.user.UserDatabaseDao
import java.io.File

@Database(entities = [DatabaseUser::class, DatabasePost::class, DatabaseReaction::class], version = 15, exportSchema = false)

@TypeConverters(Converters::class)
abstract class FaithDatabase() : RoomDatabase() {

    abstract val reactionDatabaseDao: ReactionDatabaseDao
    abstract val postDatabaseDao: PostDatabaseDao
    abstract val userDatabaseDao: UserDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: FaithDatabase? = null

        fun getInstance(context: Context): FaithDatabase {
            // Multiple threads can ask for the database at the same time, ensure we only initialize
            // it once by using synchronized. Only one thread may enter a synchronized block at a
            // time.
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FaithDatabase::class.java,
                        "faith_database"
                    )
                        // Create database from file
                        .createFromAsset("database/faith_database.db")
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }
}
