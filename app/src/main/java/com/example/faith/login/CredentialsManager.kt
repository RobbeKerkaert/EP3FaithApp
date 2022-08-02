package com.example.faith.login

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.auth0.android.result.Credentials
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey.*

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

    fun saveCredentials(context: Context, credentials: Credentials) {

        val masterKeyAlias = Builder(context, DEFAULT_MASTER_KEY_ALIAS)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()

        val preferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        editor = preferences.edit()
        editor.putString(ACCESS_TOKEN, credentials.accessToken).apply()
    }

    fun getAccessToken(context: Context): String? {

        val masterKeyAlias = Builder(context, DEFAULT_MASTER_KEY_ALIAS)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()

        val preferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return preferences.getString(ACCESS_TOKEN, null)
    }
}