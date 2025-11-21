package com.example.telegrambackup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SetupScreen(onSave: (String, String) -> Unit) {
    var apiId by remember { mutableStateOf("") }
    var apiHash by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Setup Telegram API") }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
            ) {
                OutlinedTextField(
                    value = apiId,
                    onValueChange = { apiId = it },
                    label = { Text("API ID") }
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = apiHash,
                    onValueChange = { apiHash = it },
                    label = { Text("API HASH") }
                )
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { onSave(apiId, apiHash) },
                    enabled = apiId.isNotEmpty() && apiHash.isNotEmpty()
                ) {
                    Text("Save & Continue")
                }
            }
        }
    )
}