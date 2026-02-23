package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.material.icons.filled.Map
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListScreen(viewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current
    val audioHandler = remember { AudioHandler(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finnish Vocabulary") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("map_screen") }) {
                        Icon(Icons.Default.Map, contentDescription = "View Map")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            var newPhraseText by remember { mutableStateOf("") }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newPhraseText,
                    onValueChange = { newPhraseText = it },
                    label = { Text("Add new phrase") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        viewModel.addNewPhrase(newPhraseText)
                        newPhraseText = ""
                    }
                ) {
                    Text("Add")
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.vocabularyList) { item ->
                    VocabularyCard(item = item, viewModel = viewModel, audioHandler = audioHandler)
                }
            }
        }
    }
}

@Composable
fun VocabularyCard(item: VocabularyItem, viewModel: MainViewModel, audioHandler: AudioHandler) {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isRecording = true
            val path = audioHandler.startRecording("audio_phrase_${item.id}")
            viewModel.updateItem(item.copy(audioFilePath = path))
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.phrase,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            if (isRecording) {
                IconButton(onClick = {
                    audioHandler.stopRecording()
                    isRecording = false
                }) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop Recording", tint = MaterialTheme.colorScheme.error)
                }
            } else if (item.audioFilePath != null) {
                IconButton(onClick = {
                    audioHandler.playAudio(item.audioFilePath)
                }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Play Audio", tint = MaterialTheme.colorScheme.primary)
                }
            } else {
                IconButton(onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        isRecording = true
                        val path = audioHandler.startRecording("audio_phrase_${item.id}")
                        viewModel.updateItem(item.copy(audioFilePath = path))
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }) {
                    Icon(Icons.Default.Mic, contentDescription = "Record Audio", tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}