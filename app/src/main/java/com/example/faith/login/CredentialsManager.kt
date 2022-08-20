package com.example.faith.login

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE
import androidx.security.crypto.MasterKey.DEFAULT_MASTER_KEY_ALIAS
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile

object CredentialsManager {

    private const val ACCESS_TOKEN = "access_token"

    private lateinit var editor: SharedPreferences.Editor

    private val keyGenParameterSpec = KeyGenParameterSpec.Builder(
        DEFAULT_MASTER_KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setKeySize(DEFAULT_AES_GCM_MASTER_KEY_SIZE)
        .build()

    var cachedCredentials: Credentials? = null
    var cachedUserProfile: UserProfile? = null
    var currentUserDetails: MutableMap<String, Any> = mutableMapOf<String, Any>()

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn
        get() = _isLoggedIn

    fun setLoggedIn() {
        _isLoggedIn.value = true
    }
    fun setLoggedOut() {
        _isLoggedIn.value = false
    }

    fun getUserId(): Long {
        var userId: Double = currentUserDetails["userId"] as Double
        return userId.toLong()
    }

    // Functions for metadata

    fun setUserDetails(profile: UserProfile) {
        val userMetadata = profile.getUserMetadata()
        val userId = userMetadata["userId"]
        val userName = userMetadata["userName"] as String?
        val isMonitor = userMetadata["isMonitor"]
        val userIdList = userMetadata["userIdList"] as ArrayList<Double>?
        if (userId != null && userName != null && isMonitor != null && userIdList != null) {
            currentUserDetails["userId"] = userId
            currentUserDetails["userName"] = userName.toString()
            currentUserDetails["isMonitor"] = isMonitor
            if (currentUserDetails["isMonitor"] as Boolean) {
                currentUserDetails["userIdList"] = userIdList.map {
                    it.toLong()
                }.toList()
            } else {
                currentUserDetails["userIdList"] = emptyList<Long>()
            }
        }
    }

    fun getUserDetails(): MutableMap<String, Any> {
        return currentUserDetails
    }

    // Functions for saving and getting the access token
    fun saveCredentials(context: Context, credentials: Credentials) {
        val preferences = createPreferences(context, buildMasterKey(context))

        editor = preferences.edit()
        editor.putString(ACCESS_TOKEN, credentials.accessToken).apply()
    }

    fun getAccessToken(context: Context): String? {
        val preferences = createPreferences(context, buildMasterKey(context))
        return preferences.getString(ACCESS_TOKEN, null)
    }

    // Private functions for use within the manager
    private fun buildMasterKey(context: Context): MasterKey {
        return MasterKey.Builder(context, DEFAULT_MASTER_KEY_ALIAS)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()
    }

    private fun createPreferences(context: Context, masterKeyAlias: MasterKey): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
