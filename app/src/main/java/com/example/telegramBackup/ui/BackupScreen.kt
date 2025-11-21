package com.example.telegrambackup.ui

import android.content.ContentValues
import android.provider.MediaStore
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun BackupScreen(apiId: String, apiHash: String) {
    val ctx = LocalContext.current
    var log by remember { mutableStateOf("Ready.") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Telegram Backup") }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
            ) {
                if (loading)
                    LinearProgressIndicator()

                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        loading = true
                        log = "Running…"

                        scope.launch(Dispatchers.IO) {
                            val py = Python.getInstance()
                            val module = py.getModule("telegram_backup_links")
                            val result = module.callAttr("run_backup", apiId, apiHash)

                            val html = result.toString()

                            saveToDownloads(ctx, html)

                            loading = false
                            log = "Backup complete! File saved to Downloads."
                        }
                    }
                ) {
                    Text("Run Backup")
                }

                Spacer(Modifier.height(20.dp))
                Text(log)
            }
        }
    )
}

fun saveToDownloads(ctx: android.content.Context, html: String) {
    val resolver = ctx.contentResolver
    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "telegram_links_backup.html")
        put(MediaStore.Downloads.MIME_TYPE, "text/html")
    }

    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
    uri?.let {
        resolver.openOutputStream(it)?.use { out ->
            out.write(html.toByteArray())
        }
    }
}