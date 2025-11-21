package com.example.telegrambackup.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SecureStorage(ctx: Context) {

    private val keys = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        "secure_store",
        keys,
        ctx,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveCredentials(apiId: String, apiHash: String) {
        prefs.edit()
            .putString("api_id", apiId)
            .putString("api_hash", apiHash)
            .apply()
    }

    fun getApiId() = prefs.getString("api_id", null)
    fun getApiHash() = prefs.getString("api_hash", null)
}