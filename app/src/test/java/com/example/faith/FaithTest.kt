package com.example.faith

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.faith.database.FaithDatabase
import com.example.faith.database.post.DatabasePost
import com.example.faith.database.post.PostDatabaseDao
import com.example.faith.database.reaction.DatabaseReaction
import com.example.faith.database.reaction.ReactionDatabaseDao
import com.example.faith.database.user.UserDatabaseDao
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FaithTest {
    private lateinit var postDatabaseDao: PostDatabaseDao
    private lateinit var reactionDatabaseDao: ReactionDatabaseDao
    private lateinit var userDatabaseDao: UserDatabaseDao

    private lateinit var database: FaithDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, FaithDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        postDatabaseDao = database.postDatabaseDao
        reactionDatabaseDao = database.reactionDatabaseDao
        userDatabaseDao = database.userDatabaseDao
    }

    private fun newPosts() {
        for (loop in 1..2) {
            for (post in 1..3) {
                val databasePost = DatabasePost(0, "Test$post", "Jon$post", post.toLong())
                if (loop == 2) {
                    databasePost.isFavorite = true
                }
                postDatabaseDao.insert(databasePost)
            }
        }
    }

    private fun newReactions() {
        for (loop in 1..3) {
            reactionDatabaseDao.insert(
                DatabaseReaction(
                    0,
                    loop.toLong(),
                    loop.toLong(),
                    "testreaction$loop",
                    "Jon$loop"
                )
            )
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        database.close()
    }

    // Tests

    @Test
    @Throws(Exception::class)
    fun insertPosts() {
        newPosts()
        assertEquals(6, postDatabaseDao.getCount())
    }

    @Test
    @Throws(Exception::class)
    fun removePost() {
        newPosts()
        assertEquals(6, postDatabaseDao.getCount())
        postDatabaseDao.delete(DatabasePost(1))
        assertEquals(5, postDatabaseDao.getCount())
    }

    @Test
    @Throws(Exception::class)
    fun editPost() {
        newPosts()
        assertEquals("Test1", postDatabaseDao.get(1)?.text)
        postDatabaseDao.update(
            DatabasePost(
                1,
                "Updated"
            )
        )
        assertEquals("Updated", postDatabaseDao.get(1)?.text)
    }

    @Test
    @Throws(Exception::class)
    fun addPost() {
        newPosts()
        assertEquals(6, postDatabaseDao.getCount())
        postDatabaseDao.insert(
            DatabasePost(
                0,
                "Addedpost",
                "Jef",
                4
            )
        )
        assertEquals(7, postDatabaseDao.getCount())
        assertEquals("Addedpost", postDatabaseDao.get(7)?.text)
    }

    @Test
    @Throws(Exception::class)
    fun getUserPosts() {
        newPosts()
        postDatabaseDao.getPostsByUserId(1).value?.let { assertEquals(2, it.size) }
    }

    @Test
    @Throws(Exception::class)
    fun getUserFavoritePosts() {
        newPosts()
        var userPosts = postDatabaseDao.getFavoritePostsByUserId(1)
        userPosts.value?.let { assertEquals(1, it.size) }
    }

    @Test
    @Throws(Exception::class)
    fun addFavoritePost() {
        newPosts()

        var post = postDatabaseDao.get(1)
        post!!.isFavorite = true
        postDatabaseDao.update(post)
        var newUserFavorites = postDatabaseDao.getFavoritePostsByUserId(1)
        newUserFavorites.value?.let { assertEquals(2, it.size) }
    }

    @Test
    @Throws(Exception::class)
    fun addReaction() {
        newPosts()
        newReactions()
        reactionDatabaseDao.insert(
            DatabaseReaction(
                0,
                1,
                1,
                "testreaction",
                "Jon1"
            )
        )
        assertEquals(4, reactionDatabaseDao.getCount())
    }

    @Test
    @Throws(Exception::class)
    fun removeReaction() {
        newPosts()
        newReactions()
        assertEquals(3, reactionDatabaseDao.getCount())
        reactionDatabaseDao.delete(DatabaseReaction(1))
        assertEquals(2, reactionDatabaseDao.getCount())
    }

    @Test
    @Throws(Exception::class)
    fun editReaction() {
        newPosts()
        newReactions()
        assertEquals("testreaction1", reactionDatabaseDao.get(1)?.text)
        reactionDatabaseDao.update(
            DatabaseReaction(
                1,
                1,
                1,
                "UpdatedReaction",
                "Jon1"
            )
        )
        assertEquals("UpdatedReaction", reactionDatabaseDao.get(1)?.text)
    }
}
