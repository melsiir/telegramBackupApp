package com.example.telegrambackup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.telegrambackup.ui.BackupScreen
import com.example.telegrambackup.ui.SetupScreen
import com.example.telegrambackup.util.SecureStorage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val storage = SecureStorage(this)

        setContent {
            val apiId = storage.getApiId()
            val apiHash = storage.getApiHash()

            if (apiId.isNullOrEmpty() || apiHash.isNullOrEmpty()) {
                SetupScreen(onSave = { id, hash ->
                    storage.saveCredentials(id, hash)
                })
            } else {
                BackupScreen(apiId, apiHash)
            }
        }
    }
}