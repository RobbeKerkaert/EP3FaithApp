package com.example.faith.login

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.auth0.android.result.Credentials
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.*
import com.auth0.android.jwt.JWT
import com.auth0.android.result.UserProfile
import com.example.faith.domain.User

object CredentialsManager {

    private const val ACCESS_TOKEN = "access_token"

    private lateinit var editor: SharedPreferences.Editor

    private val keyGenParameterSpec = KeyGenParameterSpec.Builder(
        DEFAULT_MASTER_KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setKeySize(DEFAULT_AES_GCM_MASTER_KEY_SIZE)
        .build()

    var currentUserDetails: MutableMap<String, Any> = mutableMapOf<String, Any>()

    // Functions for metadata
    fun setUserDetails(userDetails: MutableMap<String, Any>) {
        currentUserDetails = userDetails
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
        return Builder(context, DEFAULT_MASTER_KEY_ALIAS)
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